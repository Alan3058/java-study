package com.ctosb.study.chat.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 用户信息类
 *
 * @author Alan
 */
public class UserInfo extends User {

    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private Socket socket;

    public UserInfo() {

    }

    public UserInfo(Socket socket, ObjectInputStream reader, ObjectOutputStream writer, User user) {
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }

    public User getUser() {
        return new User(getUserName(), getPassword());
    }

    public void setUser(User user) {
        setUserName(user.getUserName());
        setPassword(user.getPassword());
    }

    public ObjectInputStream getReader() {
        return reader;
    }

    public void setReader(ObjectInputStream reader) {
        this.reader = reader;
    }

    public ObjectOutputStream getWriter() {
        return writer;
    }

    public void setWriter(ObjectOutputStream writer) {
        this.writer = writer;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void close() {
        try {
            if (reader != null) {
                this.reader.close();
            }
            if (writer != null) {
                this.writer.close();
            }
            if (socket != null) {
                this.socket.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
