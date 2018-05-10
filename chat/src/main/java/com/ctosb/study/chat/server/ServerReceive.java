package com.ctosb.study.chat.server;

import com.ctosb.study.chat.model.Message;
import com.ctosb.study.chat.model.UserInfo;
import com.ctosb.study.chat.util.StaticUtil;
import com.ctosb.study.chat.util.UserUtil;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 * 服务端接受客户端的消息
 *
 * @author Alan
 */
public class ServerReceive implements Runnable {

    private Server server;
    private UserInfo userInfo;

    public ServerReceive(Server server, UserInfo userInfo) {
        this.server = server;
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
                msg = (Message) reader.readObject();
                if (Message.OFF_LINE.equals(msg.getCommand())) {
                    //客户端离线
                    this.server.showMsg.append(userInfo.getUserName() + "已下线！" + StaticUtil.NEWLINE);
                    this.server.userList.removeItem(userInfo.getUserName());
                    this.userInfo.close();
                    Server.USER_INFOS.remove(userInfo);
                    //分发新的用户列表给客户端
                    Message msg_send = new Message();
                    msg_send.setCommand(Message.FLUSH_USER);
                    msg_send.setData(UserUtil.getComboBoxModelByUser(Server.USER_INFOS));
                    UserUtil.distributeMsg(Server.USER_INFOS, msg_send);
                    break;
                } else if (Message.SEND_MSG.equals(msg.getCommand())) {
                    //发送消息
                    if (StaticUtil.ALL.equals(msg.getDest())) {
                        //分发消息给所有客户端
                        Message msg_send = new Message();
                        msg_send.setCommand(Message.SEND_MSG);
                        msg_send.setMessage(msg.getMessage());
                        UserUtil.distributeMsg(Server.USER_INFOS, msg_send);
                    } else {
                        //发送消息给客户端
                        Message msg_send = new Message();
                        msg_send.setCommand(Message.SEND_MSG);
                        msg_send.setMessage(msg.getMessage());
                        for (UserInfo e : Server.USER_INFOS) {
                            if (msg.getDest().equals(e.getUserName())) {
                                ObjectOutputStream out = e.getWriter();
                                out.writeObject(msg_send);
                                out.flush();
                            }
                        }
                    }
                    this.server.showMsg.append(userInfo.getUserName() + " 说:" + msg.getMessage() + StaticUtil.NEWLINE);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
//			e.printStackTrace();
        }
    }

}
