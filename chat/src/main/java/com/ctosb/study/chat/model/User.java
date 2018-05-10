package com.ctosb.study.chat.model;

import java.io.Serializable;

/**
 * 用户类
 *
 * @author Alan
 */
public class User implements Serializable {

    private String userName;
    private String password;

    public User() {

    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
