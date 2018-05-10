package com.ctosb.study.chat.util;

import com.ctosb.study.chat.model.Message;
import com.ctosb.study.chat.model.User;
import com.ctosb.study.chat.model.UserInfo;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * 用户帮助类
 *
 * @author Alan
 */
public class UserUtil {
    /**
     * 判断用户是否存在
     *
     * @param userInfos
     * @param user
     * @return
     */
    public static boolean isExist(List<UserInfo> userInfos, User user) {
        for (UserInfo e : userInfos) {
            if (e.getUserName().equals(user.getUserName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取下拉框的数据集
     *
     * @param userInfos
     * @return
     */
    public static DefaultComboBoxModel getComboBoxModelByUser(List<UserInfo> userInfos) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(StaticUtil.ALL);
        for (UserInfo e : userInfos) {
            model.addElement(e.getUserName());
        }
        return model;
    }

    /**
     * 分发消息
     *
     * @param userInfos
     * @param obj
     */
    public static void distributeMsg(List<UserInfo> userInfos, Message msg) {
        for (UserInfo e : userInfos) {
            ObjectOutputStream writer = e.getWriter();
            try {
                writer.writeObject(msg);
                writer.flush();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
}
