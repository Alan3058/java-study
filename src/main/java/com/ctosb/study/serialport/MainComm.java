package com.ctosb.study.serialport;

import com.ctosb.study.serialport.HttpServer.Callbackable;

public class MainComm {

    public static void main(String[] args) {
        final SerialPortComm serialPortComm = new SerialPortComm();
        serialPortComm.start();

        HttpServer httpServer = new HttpServer(new Callbackable() {
            @Override
            public String getData() {
                String content = serialPortComm.getContent();
                content = content.replace(System.getProperty("line.separator"), "");
                String splitArray[] = content.split(" ");
                StringBuffer sb = new StringBuffer();
                for (String ele : splitArray) {
                    if ("".equals(ele)) {
                        continue;
                    }
                    int a = Integer.parseInt(ele);
                    char ab = (char) a;
                    sb.append(ab);
                }
                return sb.toString();
            }
        });
        httpServer.startServer();
    }
}
