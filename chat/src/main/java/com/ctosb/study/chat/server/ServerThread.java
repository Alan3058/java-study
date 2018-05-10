package com.ctosb.study.chat.server;

import com.ctosb.study.chat.model.Message;
import com.ctosb.study.chat.model.User;
import com.ctosb.study.chat.model.UserInfo;
import com.ctosb.study.chat.util.StaticUtil;
import com.ctosb.study.chat.util.UserUtil;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * 服务端接收客户端的Socket连接请求
 *
 * @author Alan
 */
public class ServerThread implements Runnable {
    private Server server;
    private ServerSocket serverSocket;

    public ServerThread(Server server, ServerSocket serverSocket) {
        this.server = server;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
                //客户端登录时会发送用户信息到服务端来
                User user = (User) reader.readObject();
                UserInfo userInfo = new UserInfo(socket, reader, writer, user);
                Message msg = new Message();
                if (UserUtil.isExist(Server.USER_INFOS, user)) {
                    //如果该用户已登录，则连接失败
                    msg.setCommand(Message.CONN_FAIL);
                    msg.setMessage("用户已在线！" + StaticUtil.NEWLINE);
                    writer.writeObject(msg);
                    writer.flush();
                    userInfo.close();
                    continue;
                } else {
                    //连接成功
                    msg.setCommand(Message.CONN_SUCC);
                    writer.writeObject(msg);
                    writer.flush();
                }
                Server.USER_INFOS.add(userInfo);
                server.userList.addItem(user.getUserName());
                server.showMsg.append(user.getUserName() + "上线啦！" + StaticUtil.NEWLINE);
                //分发新的用户列表给客户端
                msg = new Message();
                msg.setCommand(Message.FLUSH_USER);
                msg.setData(UserUtil.getComboBoxModelByUser(Server.USER_INFOS));
                UserUtil.distributeMsg(Server.USER_INFOS, msg);
                //启动服务器接受消息的线程
                new Thread(new ServerReceive(server, userInfo)).start();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
    }

}
