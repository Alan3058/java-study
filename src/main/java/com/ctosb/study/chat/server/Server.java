package com.ctosb.study.chat.server;

import com.ctosb.study.chat.model.Message;
import com.ctosb.study.chat.model.UserInfo;
import com.ctosb.study.chat.util.StaticUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;


/**
 * 服务端窗口
 *
 * @author Alan
 */
public class Server extends JFrame implements ActionListener {

    private int width = 400;
    private int height = 500;
    //网络socket
    private ServerSocket serverSocket;
    private int port = 8888;
    //用户信息集合
    public static List<UserInfo> USER_INFOS = new ArrayList<UserInfo>();

    //菜单栏
    private JMenuBar menubar;
    private JMenu menu_help;
    private JMenuItem menuitem_about;
    private JMenu menu_setting;
    private JMenuItem menuitem_port;

    //工具栏
    private JToolBar toolbar;
    private JButton button_connect;
    private JButton button_stop;
    private JButton button_exit;

    //中间显示消息块
    JTextArea showMsg;
    private JScrollPane showMsg_ScrollBar;

    //发送消息部分
//	private JComboBox faceList;
    JComboBox userList;
    private JPanel downPanel;
    private JTextField sendMsg;
    private JButton send;

    public Server(String name) {
        super(name);
        //初始化窗口
        init();
    }


    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Server client = new Server("服务端");
//		client.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        Object obj = e.getSource();
        if (obj.equals(button_connect)) {
            //启动服务
            start();
        } else if (obj.equals(send)) {
            //发送消息
            sendMsg();
        } else if (obj.equals(button_stop)) {
            //停止服务
            stop();
        } else if (obj.equals(button_exit)) {
            //退出系统
            int rs = JOptionPane.showConfirmDialog(this, "确定要退出系统吗", "退出", JOptionPane.YES_NO_OPTION);
            if (rs == JOptionPane.YES_OPTION) {
                this.stop();
                dispose();
                System.exit(0);
            }

        } else if (obj.equals(menuitem_port)) {
            //端口设置
            PortConfig portConfig = new PortConfig(this, port);
            portConfig.setVisible(true);
            port = portConfig.result();
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
        menuitem_port = new JMenuItem("端口");

        menu_setting.add(menuitem_port);
        menu_help.add(menuitem_about);
        menubar.add(menu_setting);
        menubar.add(menu_help);

        //工具栏
        toolbar = new JToolBar();
        button_connect = new JButton("启动");
        button_stop = new JButton("停止");
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
//		faceList = new JComboBox();
//		faceList.addItem("高兴地");
//		faceList.addItem("惊讶地");
//		faceList.addItem("微笑着");
//		faceList.addItem("愤怒地");
//		faceList.addItem("悲伤地");
//		faceList.addItem("忧郁地");

        userList = new JComboBox();
        userList.addItem(StaticUtil.ALL);
        for (UserInfo e : USER_INFOS) {
            userList.addItem(e.getUserName());
        }

        downPanel = new JPanel();
        sendMsg = new JTextField(23);
        send = new JButton("发送");

//		downPanel.add(faceList);
        downPanel.add(userList);
        downPanel.add(sendMsg);
        downPanel.add(send);

        //模块拼接
        setJMenuBar(menubar);
        add(toolbar, BorderLayout.NORTH);
        add(showMsg_ScrollBar, BorderLayout.CENTER);
        add(downPanel, BorderLayout.SOUTH);

        //监听器
        menuitem_port.addActionListener(this);
        menuitem_about.addActionListener(this);
        button_connect.addActionListener(this);
        button_stop.addActionListener(this);
        button_exit.addActionListener(this);
        send.addActionListener(this);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // TODO Auto-generated method stub
                super.windowClosing(e);
                stop();
                dispose();
                System.exit(0);
            }

        });
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

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭窗口时同时关闭程序
        Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();//本地屏幕的中心点
        this.setBounds(p.x - width / 2, p.y - height / 2, width, height);
        setVisible(true);//窗口可见
    }

    /**
     * 启动服务器
     *
     * @author Alan
     * @date 2014-4-15
     */
    private void start() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            // TODO Auto-generated catch block
//			e.printStackTrace();
            JOptionPane.showMessageDialog(this, "启动服务器失败，请检查网络连接或端口设置！");
            return;
        }
        showMsg.append("启动服务器成功！" + StaticUtil.NEWLINE);
        new Thread(new ServerThread(this, serverSocket)).start();
    }

    /**
     * 发送消息
     *
     * @author Alan
     * @date 2014-4-15
     */
    private void sendMsg() {
        try {
            String text = sendMsg.getText();
            StringBuffer sb = new StringBuffer();
            if ("".equals(text)) {
                return;
            }
            Message msg = new Message();
            msg.setMessage("系统消息：" + text);
            msg.setCommand(Message.SEND_MSG);
            String userName = userList.getSelectedItem().toString();
            if (StaticUtil.ALL.equals(userName)) {
                //系统消息分发
                for (UserInfo e : USER_INFOS) {
                    ObjectOutputStream writer = e.getWriter();
                    writer.writeObject(msg);
                    writer.flush();
                    sb.append("   用户名：" + e.getUserName() + " 地址：" + e.getSocket().getInetAddress() + " 端口：" + e.getSocket().getPort() + StaticUtil.NEWLINE);
                }
            } else {
                for (UserInfo e : USER_INFOS) {
                    if (userName.equals(e.getUserName())) {
                        ObjectOutputStream writer = e.getWriter();
                        writer.writeObject(msg);
                        writer.flush();
                        sb.append("   用户名：" + e.getUserName() + " 地址：" + e.getSocket().getInetAddress() + " 端口：" + e.getSocket().getPort() + StaticUtil.NEWLINE);
                    }
                }
            }
            if (!"".equals(sb.toString())) {
                showMsg.append("消息已分发至以下用户：" + StaticUtil.NEWLINE + sb.toString());
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 停止服务
     *
     * @author Alan
     * @date 2014-4-15
     */
    private void stop() {
        if (serverSocket == null || serverSocket.isClosed()) {
            return;
        }
        try {
            DefaultComboBoxModel model = new DefaultComboBoxModel();
            model.addElement(StaticUtil.ALL);
            Message msg = new Message();
            msg.setCommand(Message.SERVER_STOP);
            msg.setMessage("服务器已停止，可能正在维护中，请稍后连接！");
            msg.setData(model);
            for (UserInfo e : USER_INFOS) {
                ObjectOutputStream writer = e.getWriter();
                writer.writeObject(msg);
                writer.flush();
                e.close();
            }
            userList.setModel(model);
            USER_INFOS.removeAll(USER_INFOS);
            serverSocket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
//			e.printStackTrace();
            showMsg.append("关闭服务器失败，请检查服务器是否已启动！" + StaticUtil.NEWLINE);
            return;
        }
        showMsg.append("关闭服务器成功！" + StaticUtil.NEWLINE);
    }

}
