package com.ctosb.study.serialport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    private Logger log = LoggerFactory.getLogger(getClass());
    private ServerSocket serverSocket;
    private Socket socket;
    private Callbackable callbackable;

    public HttpServer(Callbackable callbackable) {
        this.callbackable = callbackable;
    }

    /**
     * 启动服务
     *
     * @author Alan
     * @createTime 2016年1月29日 下午1:16:53
     */
    public void startServer() {
        try {
            serverSocket = new ServerSocket(9999);
            log.info("HttpServer服务启动成功");
        } catch (IOException e) {
            log.error("服务启动失败:", e);
            return;
        }
        Thread thread = new Thread(new Run());
        thread.setName("Thread-HttpServer");
        thread.start();
    }

    /**
     * 运行线程
     *
     * @author Alan
     */
    private class Run implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    log.error("ServerSocket 接收异常：", e);
                    return;
                }
                HttpHelper http = null;
                try {
                    http = new HttpHelper(socket.getInputStream(), socket.getOutputStream());
                    http.sendData(callbackable.getData());
                } catch (IOException e) {
                    log.error("获取客户端socket输入输出流失败:", e);
                    return;
                }
                // 关闭释放资源
                close();
            }
        }
    }

    /**
     * 关闭释放资源
     *
     * @author Alan
     * @createTime 2016年1月29日 下午1:20:04
     */
    private void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            log.error("关闭socket失败", e);
        }
    }

    /**
     * 获取回写数据接口
     *
     * @author Alan
     */
    public interface Callbackable {
        public String getData();
    }

}
