package com.ctosb.study.chat.server;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 端口设置
 *
 * @author Alan
 */
public class PortConfig extends JDialog {
    private int width = 200;
    private int height = 150;
    //输入区域
    private JPanel upPanel;
    private JLabel portLabel;
    private JTextField portText;
    //按钮区域
    private JPanel downPanel;
    private JButton confirmButton;
    private JButton cancelButton;
    private int port;


    public PortConfig(JFrame frame, int port) {
        super(frame, true);
        this.port = port;
        init();
    }

    /**
     * 初始化对话框
     *
     * @author Alan
     * @date 2014-4-18
     */
    private void init() {
        this.setTitle("连接设置");
        this.setLayout(new BorderLayout());
        //输入区域
        upPanel = new JPanel();
        upPanel.setLayout(new GridLayout(2, 2));
        portLabel = new JLabel("输入端口号：");
        portText = new JTextField();
        portText.setDocument(new IntegerDocument()); //设置端口只能输入数字
        portText.setText(port + "");

        upPanel.add(portLabel);
        upPanel.add(portText);

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
                port = Integer.parseInt(portText.getText());
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
    public int result() {
        return port;
    }

    /**
     * 只允许输入数字Document类
     *
     * @author Alan
     */
    class IntegerDocument extends PlainDocument {
        public void insertString(int offset, String s, AttributeSet attributeSet) throws BadLocationException {
            try {
                Integer.parseInt(s);
            } catch (Exception ex) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            super.insertString(offset, s, attributeSet);
        }
    }


    public static void main(String[] args) {
        new PortConfig(null, 8888).setVisible(true);
    }

}
