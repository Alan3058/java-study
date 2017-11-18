package com.ctosb.study.chat.client;

import com.ctosb.study.chat.model.Message;
import com.ctosb.study.chat.model.User;
import com.ctosb.study.chat.model.UserInfo;
import com.ctosb.study.chat.util.StaticUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


/**
 * 客户端的窗口
 *
 * @author Alan
 */
public class Client extends JFrame implements ActionListener {

    private int width = 400;
    private int height = 500;
    //网络socket
//	Socket socket;
//	ObjectInputStream reader;
//	ObjectOutputStream writer;
    //默认用户名和地址
    private InetAddress local;
    private UserInfo userInfo = new UserInfo();
    private NetAddress addr;
    public static List<UserInfo> USER_INFOS = new ArrayList<UserInfo>();

    //菜单栏
    private JMenuBar menubar;
    private JMenu menu_help;
    private JMenuItem menuitem_about;
    private JMenu menu_setting;
    private JMenuItem menuitem_server;
    private JMenuItem menuitem_user;

    //工具栏
    private JToolBar toolbar;
    JButton button_connect;
    private JButton button_stop;
    private JButton button_exit;

    //中间显示消息块
    JTextArea showMsg;
    private JScrollPane showMsg_ScrollBar;

    //发送消息部分
    private JComboBox faceList;
    JComboBox userList;
    private JPanel downPanel;
    private JTextField sendMsg;
    private JButton send;

    public Client(String name) {
        super(name);
        //默认用户名和地址
        try {
            local = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        userInfo.setUser(new User(local.getHostName(), System.getProperty("user.name")));
        addr = new NetAddress(local.getHostAddress(), 8888);
        //初始化窗口
        init();
    }


    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Client client = new Client("客户端");
//		client.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        Object obj = e.getSource();
        if (obj.equals(button_connect)) {
            //连接
            connect();
        } else if (obj.equals(send)) {
            //发送消息
            sendMsg();
        } else if (obj.equals(button_stop)) {
            //断开连接
            disConnect();
        } else if (obj.equals(button_exit)) {
            //退出系统
            int rs = JOptionPane.showConfirmDialog(this, "确定要退出系统吗", "退出", JOptionPane.YES_NO_OPTION);
            if (rs == JOptionPane.YES_OPTION) {
                this.disConnect();
                this.dispose();
                System.exit(0);
            }

        } else if (obj.equals(menuitem_server)) {
            //连接设置
            ConnectConfig conn = new ConnectConfig(this, addr);
            conn.setVisible(true);
            addr = conn.result();
        } else if (obj.equals(menuitem_user)) {
            //用户设置
            UserConfig userConfig = new UserConfig(this, userInfo);
            userConfig.setVisible(true);
            userInfo.setUser(userConfig.result());
        } else if (obj.equals(menuitem_about)) {
            //关于
            new AboutMsg(this).setVisible(true);
        }

    }

    /**
     * 初始化窗口
     *
     * @author Alan
     * @date 2014-4-15
     */
    private void init() {
        //菜单栏
        menubar = new JMenuBar();
        menu_help = new JMenu("帮助");
        menuitem_about = new JMenuItem("关于");
        menu_setting = new JMenu("设置");
        menuitem_server = new JMenuItem("服务器");
        menuitem_user = new JMenuItem("用户");

        menu_setting.add(menuitem_server);
        menu_setting.add(menuitem_user);
        menu_help.add(menuitem_about);
        menubar.add(menu_setting);
        menubar.add(menu_help);

        //工具栏
        toolbar = new JToolBar();
        button_connect = new JButton("登录");
        button_stop = new JButton("注销");
        button_exit = new JButton("退出");

        toolbar.add(button_connect);
        toolbar.add(button_stop);
        toolbar.add(button_exit);

        //中间显示消息块
        showMsg = new JTextArea();
        showMsg_ScrollBar = new JScrollPane(showMsg);
        showMsg.setEditable(false);
        showMsg_ScrollBar.setPreferredSize(new Dimension(100, 300));

        //发送消息块
        faceList = new JComboBox();
        faceList.addItem("高兴地");
        faceList.addItem("惊讶地");
        faceList.addItem("微笑着");
        faceList.addItem("愤怒地");
        faceList.addItem("悲伤地");
        faceList.addItem("忧郁地");

        userList = new JComboBox();
        userList.addItem(StaticUtil.ALL);
        for (UserInfo e : USER_INFOS) {
            userList.addItem(e.getUserName());
        }

        downPanel = new JPanel();
        sendMsg = new JTextField(23);
        send = new JButton("发送");

        downPanel.add(faceList);
        downPanel.add(userList);
        downPanel.add(sendMsg);
        downPanel.add(send);

        //模块拼接
        setJMenuBar(menubar);
        add(toolbar, BorderLayout.NORTH);
        add(showMsg_ScrollBar, BorderLayout.CENTER);
        add(downPanel, BorderLayout.SOUTH);

        //监听器
        menuitem_server.addActionListener(this);
        menuitem_user.addActionListener(this);
        menuitem_about.addActionListener(this);
        button_connect.addActionListener(this);
        button_stop.addActionListener(this);
        button_exit.addActionListener(this);
        send.addActionListener(this);
        sendMsg.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                char c = e.getKeyChar();
                if (c == KeyEvent.VK_ENTER) {
                    sendMsg();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

        });
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // TODO Auto-generated method stub
                super.windowClosing(e);
                disConnect();
                dispose();
                System.exit(0);
            }

        });
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭窗口时同时关闭程序
        Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();//本地屏幕的中心点
        this.setBounds(p.x - width / 2, p.y - height / 2, width, height);

        this.setVisible(true);//窗口可见
    }

    /**
     * 连接服务器
     *
     * @author Alan
     * @date 2014-4-15
     */
    private void connect() {
        Socket socket = null;
        try {
            if (local.getHostAddress().equals(addr.getIp())) {
                socket = new Socket("localhost", addr.getPort());
            } else {
                socket = new Socket(addr.getIp(), addr.getPort());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(this, "登录失败，请检查服务器设置" + StaticUtil.NEWLINE);
            return;
        }
        try {
            ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
            userInfo.setSocket(socket);
            userInfo.setReader(reader);
            userInfo.setWriter(writer);
            writer.writeObject(userInfo.getUser());
            writer.flush();
            Message msg = (Message) reader.readObject();
            if (Message.CONN_FAIL.equals(msg.getCommand())) {
                userInfo.close();
                showMsg.append(msg.getMessage());
                return;
            }
            showMsg.append("用户登录成功！" + StaticUtil.NEWLINE);
            button_connect.setEnabled(false);
            //启动客户端接受消息线程
            new Thread(new ClientReceive(this, userInfo)).start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     *
     * @author Alan
     * @date 2014-4-15
     */
    private void sendMsg() {
        try {
            if (userInfo.getSocket() == null || userInfo.getSocket().isClosed()) {
                JOptionPane.showMessageDialog(this, "用户已离线!");
                return;
            }
            String text = sendMsg.getText();
            if ("".equals(text)) {
                return;
            }
            Message msg = new Message();
            msg.setCommand(Message.SEND_MSG);
            msg.setMessage(userInfo.getUserName() + faceList.getSelectedItem().toString() + "对" + userList.getSelectedItem().toString() + "说：" + text);
            msg.setDest(userList.getSelectedItem().toString());
            ObjectOutputStream writer = userInfo.getWriter();

            writer.writeObject(msg);
            writer.flush();
//			showMsg.append("我说:"+text+StringUtil.NEWLINE);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 断开连接
     *
     * @author Alan
     * @date 2014-4-15
     */
    private void disConnect() {
        try {
            Socket socket = userInfo.getSocket();
            if (socket != null && !socket.isClosed()) {
                ObjectOutputStream writer = userInfo.getWriter();
                Message msg_send = new Message();
                msg_send.setCommand(Message.OFF_LINE);
                writer.writeObject(msg_send);
                writer.flush();
                this.button_connect.setEnabled(true);
                this.showMsg.append("用户已注销！" + StaticUtil.NEWLINE);
                userInfo.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
