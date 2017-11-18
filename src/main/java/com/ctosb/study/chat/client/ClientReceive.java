package com.ctosb.study.chat.client;

import com.ctosb.study.chat.model.Message;
import com.ctosb.study.chat.model.UserInfo;
import com.ctosb.study.chat.util.StaticUtil;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * 客户端接受服务端的消息
 *
 * @author Alan
 */
public class ClientReceive implements Runnable {

    private Client client;
    private UserInfo userInfo;

    public ClientReceive(Client client, UserInfo userInfo) {
        this.client = client;
        this.userInfo = userInfo;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            Socket socket = userInfo.getSocket();
            ObjectInputStream reader = userInfo.getReader();
            while (!socket.isClosed()) {
                Message msg = null;
                try {
                    msg = (Message) reader.readObject();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (Message.FLUSH_USER.equals(msg.getCommand())) {
                    this.client.userList.removeAllItems();
                    this.client.userList.setModel((ComboBoxModel) msg.getData());
                } else if (Message.SEND_MSG.equals(msg.getCommand())) {
                    this.client.showMsg.append(msg.getMessage() + StaticUtil.NEWLINE);
                } else if (Message.SERVER_STOP.equals(msg.getCommand())) {
                    this.client.userList.setModel((ComboBoxModel) msg.getData());
                    this.client.showMsg.append(msg.getMessage() + StaticUtil.NEWLINE);
                    this.client.button_connect.setEnabled(true);
                    Client.USER_INFOS.removeAll(Client.USER_INFOS);
                    userInfo.close();
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
//			e.printStackTrace();
        }
    }

}
