package com.ctosb.study.chat.model;

import java.io.Serializable;

/**
 * 消息类，用来服务端和客户端传输消息的类
 *
 * @author Alan
 */
public class Message implements Serializable {

    public final static String SEND_MSG = "send_msg";
    public final static String FLUSH_USER = "flush_user";
    public final static String CONN_SUCC = "conn_succ";
    public final static String CONN_FAIL = "conn_fail";
    public final static String ON_LINE = "on_line";
    public final static String OFF_LINE = "off_line";
    public final static String SERVER_START = "server_start";
    public final static String SERVER_STOP = "server_stop";


    private boolean isSuccess;
    private Object data;
    private String message;
    private String dest;
    private String command;

    public Message() {

    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

}
