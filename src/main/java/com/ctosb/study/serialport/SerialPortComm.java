package com.ctosb.study.serialport;

import gnu.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;


public class SerialPortComm {

    private Logger log = LoggerFactory.getLogger(getClass());

    // 检测系统中可用的通讯端口类
    private CommPortIdentifier commPortIdentifier;
    // 输入输出流
    private InputStream inputStream;
    private OutputStream outputStream;
    // RS-232的串行口
    private SerialPort serialPort;
    private StringBuffer byteContent = new StringBuffer();// 存储接收字节

    public static final String PARAMS_DELAY = "delay read"; // 延时等待端口数据准备的时间
    public static final String PARAMS_TIMEOUT = "timeout"; // 超时时间
    public static final String PARAMS_PORT = "port name"; // 端口名称
    public static final String PARAMS_DATABITS = "data bits"; // 数据位
    public static final String PARAMS_STOPBITS = "stop bits"; // 停止位
    public static final String PARAMS_PARITY = "parity"; // 奇偶校验
    public static final String PARAMS_RATE = "rate"; // 波特率

    private static Map<String, Object> params = new HashMap<String, Object>();

    public SerialPortComm() {
        initParams();
        initPort();
    }

    /**
     * 启动com口写数据线程
     *
     * @author Alan
     * @createTime 2016年1月29日 下午2:53:12
     */
    public void start() {
        Thread thread = new Thread(new CommWriter());
        thread.setName("Thread-SerialPortComm");
        thread.start();
    }

    /**
     * 初始化串口
     *
     * @author Alan
     * @createTime 2016年1月28日 下午10:11:58
     */
    public void initPort() {
        try {
            commPortIdentifier = CommPortIdentifier.getPortIdentifier((String) params.get(PARAMS_PORT));
        } catch (NoSuchPortException e) {
            log.error("没有该COM端口：", e);
            return;
        }
        try {
            serialPort = (SerialPort) commPortIdentifier.open("SerialPort", (Integer) params.get(PARAMS_TIMEOUT));
        } catch (PortInUseException e) {
            log.error("COM端口已被占用：", e);
            return;
        }
        try {
            serialPort.addEventListener(new CommReader());
        } catch (TooManyListenersException e) {
            log.error("串口监听器过多：", e);
            return;
        }
        serialPort.notifyOnDataAvailable(true);
        /* 设置串口通讯参数 */
        try {
            serialPort.setSerialPortParams((Integer) params.get(PARAMS_RATE), (Integer) params.get(PARAMS_DATABITS),
                    (Integer) params.get(PARAMS_STOPBITS), (Integer) params.get(PARAMS_PARITY));
        } catch (UnsupportedCommOperationException e) {
            log.error("不支持该COM操作", e);
            return;
        }
        try {
            outputStream = serialPort.getOutputStream();
            inputStream = serialPort.getInputStream();
        } catch (IOException e) {
            log.error("获取COM端口输入输出流异常：", e);
        }
    }

    public void initParams() {
        int delay = 900;
        int parity = 0;
        String port = "COM9";
        int timeout = 2000;
        int stopbits = 1;
        int rate = 600;
        int databits = 8;
        params.put(PARAMS_DELAY, delay);
        params.put(PARAMS_PARITY, parity);
        params.put(PARAMS_PORT, port);
        params.put(PARAMS_RATE, rate);
        params.put(PARAMS_STOPBITS, stopbits);
        params.put(PARAMS_TIMEOUT, timeout);
        params.put(PARAMS_DATABITS, databits);

    }

    public String getContent() {
        String content = byteContent.toString();
        // 清空内容
        byteContent.setLength(0);
        ;
        return content;
    }

    /**
     * 获取可用序列Com口
     *
     * @return
     * @author Alan
     * @createTime 2016年1月28日 下午10:41:56
     */
    public List<CommPortIdentifier> getAvailableSerialPorts() {
        List<CommPortIdentifier> commPortIdentifiers = new ArrayList<CommPortIdentifier>();
        Enumeration<CommPortIdentifier> enumeration = CommPortIdentifier.getPortIdentifiers();
        while (enumeration.hasMoreElements()) {
            CommPortIdentifier commPortIdentifier = enumeration.nextElement();
            if (commPortIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                commPortIdentifiers.add(commPortIdentifier);
            }
        }
        return commPortIdentifiers;
    }

    private class CommReader implements SerialPortEventListener {
        /**
         * 读取从串口中接收的数据
         */
        @Autowired
        public void serialEvent(SerialPortEvent event) {
            switch (event.getEventType()) {
                case SerialPortEvent.BI:
                case SerialPortEvent.OE:
                case SerialPortEvent.FE:
                case SerialPortEvent.PE:
                case SerialPortEvent.CD:
                case SerialPortEvent.CTS:
                case SerialPortEvent.DSR:
                case SerialPortEvent.RI:
                case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                    break;
                case SerialPortEvent.DATA_AVAILABLE:
                    try {
                        Thread.sleep((Integer) params.get(PARAMS_DELAY));
                    } catch (InterruptedException e) {
                        log.error(Thread.currentThread().getName() + "线程休眠异常:", e);
                    }
                    // 读取串口返回的信息
                    read();
                    break;
                default:
                    break;
            }
        }

        private void read() {
            byte[] tempBytes = new byte[1024];
            int length;
            BufferedInputStream inputStream = new BufferedInputStream(SerialPortComm.this.inputStream);
            try {
                StringBuffer tempStringBuffer = new StringBuffer();
                // while ((length = inputStream.read(tempBytes)) > 0) {
                while (inputStream.available() > 0) {
                    length = inputStream.read(tempBytes);
                    for (int i = 0; i < length; i++) {
                        tempStringBuffer.append(tempBytes[i] + " ");
                    }
                    byteContent.append(tempStringBuffer);
                    SerialPortComm.this.log.info(tempStringBuffer.toString());
                    tempStringBuffer.setLength(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private class CommWriter implements Runnable {
        /**
         * 线程发送消息
         */
        public void run() {
            while (true) {
                sendMsg(new byte[]{0x1b, 0x70});
            }
        }

        /**
         * 向串口发送字节消息
         *
         * @param msg
         * @author Alan
         * @createTime 2016年1月29日 上午12:02:10
         */
        public void sendMsg(byte[] msg) {
            try {
                outputStream.write(msg);
                try {
                    Thread.sleep((Integer) params.get(PARAMS_DELAY));
                } catch (InterruptedException e) {
                    log.error(Thread.currentThread().getName() + "线程休眠异常:", e);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
