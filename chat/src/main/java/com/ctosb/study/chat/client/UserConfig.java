package com.ctosb.study.chat.client;

import com.ctosb.study.chat.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 用户设置对话框
 *
 * @author Alan
 */
public class UserConfig extends JDialog {
    private int width = 200;
    private int height = 150;
    //输入区域
    private JPanel upPanel;
    private JLabel userNameLabel;
    private JTextField userNameText;
    private JLabel passwordLabel;
    private JTextField passwordText;
    //按钮区域
    private JPanel downPanel;
    private JButton confirmButton;
    private JButton cancelButton;

    private User user;

    public UserConfig(JFrame frame, User user) {
        super(frame, true);
        this.user = user;
        init();
    }

    /**
     * 初始化对话框
     *
     * @author Alan
     * @date 2014-4-18
     */
    private void init() {
        this.setTitle("用户修改");
        this.setLayout(new BorderLayout());
        //输入区域
        upPanel = new JPanel();
        upPanel.setLayout(new GridLayout(2, 2));
        userNameLabel = new JLabel("输入用户名：");
        userNameText = new JTextField(user.getUserName());
        passwordLabel = new JLabel("输入密码：");
        passwordText = new JTextField(user.getPassword());

        upPanel.add(userNameLabel);
        upPanel.add(userNameText);
        upPanel.add(passwordLabel);
        upPanel.add(passwordText);
        //按钮区域
        downPanel = new JPanel();
        confirmButton = new JButton("确认");
        cancelButton = new JButton("取消");
        downPanel.add(confirmButton);
        downPanel.add(cancelButton);
        //模块拼接
        this.add(upPanel, BorderLayout.NORTH);
        this.add(downPanel, BorderLayout.SOUTH);

        Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();//本地屏幕的中心点
        this.setBounds(p.x - width / 2, p.y - height / 2, width, height);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        //确认按钮添加监听器
        confirmButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                user.setUserName(userNameText.getText());
                user.setPassword(passwordText.getText());
                dispose();
            }

        });
        //取消按钮添加监听器
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                dispose();
            }

        });
    }

    /**
     * 返回结果
     *
     * @return
     * @author Alan
     * @date 2014-4-18
     */
    public User result() {
        return user;
    }

    public static void main(String[] args) {
        new UserConfig(null, new User("alan", "1")).setVisible(true);
    }

}
