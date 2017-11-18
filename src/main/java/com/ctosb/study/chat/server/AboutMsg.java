package com.ctosb.study.chat.server;

import com.ctosb.study.chat.util.StaticUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 关于的消息框
 *
 * @author Alan
 */
public class AboutMsg extends JDialog {
    private int width = 200;
    private int height = 150;
    private JPanel upPanel;
    private JLabel label;

    private JTextArea content;

    private JPanel downPanel;
    private JButton confirmButton;
    private Color bg = Color.BLACK;

    public AboutMsg(JFrame frame) {
        super(frame);
        init();
    }

    private void init() {
        this.setTitle("关于");
        this.setLayout(new BorderLayout());
        //上标签
        upPanel = new JPanel();
        label = new JLabel("版权通知");
        upPanel.add(label);
        //中间内容
        content = new JTextArea();
        content.setEditable(false);
        content.append("        本系统解释权归Alan（李连钢）所有，" + StaticUtil.NEWLINE + "请购买正品，翻版必究！");
        //确认按钮
        downPanel = new JPanel();
        confirmButton = new JButton("确认");
        downPanel.add(confirmButton);

        //设置颜色
        upPanel.setBackground(bg);
        label.setForeground(Color.CYAN);
        content.setBackground(bg);
        content.setForeground(Color.RED);
        downPanel.setBackground(bg);
        confirmButton.setBackground(bg);
        confirmButton.setForeground(Color.CYAN);

        //拼接模块
        this.add(upPanel, BorderLayout.NORTH);
        this.add(content, BorderLayout.CENTER);
        this.add(downPanel, BorderLayout.SOUTH);

        Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();//本地屏幕的中心点
        this.setBounds(p.x - width / 2, p.y - height / 2, width, height);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        //监听按钮
        confirmButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        new AboutMsg(null).setVisible(true);
    }

}
