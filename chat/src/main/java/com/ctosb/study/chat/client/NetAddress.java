package com.ctosb.study.chat.client;

/**
 * 封装ip和port端口
 *
 * @author Alan
 */
public class NetAddress {

    private String ip;
    private int port;

    public NetAddress(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
