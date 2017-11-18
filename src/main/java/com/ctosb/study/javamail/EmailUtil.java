package com.ctosb.study.javamail;

import org.apache.commons.mail.*;

import java.net.URL;

public class EmailUtil {

    /**
     * @param args
     * @throws Exception
     * @author Alan
     * @time 2015-10-18 下午12:53:57
     */
    public static void main(String[] args) throws Exception {
        sendMail();
//		sendMultiMail();
        sendHtmlMail();
    }

    /**
     * 发送普通邮件
     *
     * @throws Exception
     * @author Alan
     * @time 2015-10-27 下午03:25:17
     */
    public static void sendMail() throws Exception {
        SimpleEmail email = new SimpleEmail();
        buildEmail(email);
        email.setSubject("测试普通邮件");
        email.setMsg("测试普通邮件");
        email.send();
    }

    /**
     * 发送带附件的邮件
     *
     * @throws Exception
     * @author Alan
     * @time 2015-10-27 下午03:25:28
     */
    public static void sendMultiMail() throws Exception {
        EmailAttachment attachment = new EmailAttachment();//远程附件
        attachment.setURL(new URL(
                "http://www.apache.org/images/asf_logo_wide.gif"));
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription("Apache logo");
        attachment.setName("Apache logo");

        EmailAttachment attachmentlocal = new EmailAttachment();//本地附件
        attachmentlocal.setPath("H:/workspaceST/study/src/main/java/com/ctosb/study/javamail/MailUtil.java");
        attachmentlocal.setDescription("本地文件");
        attachmentlocal.setName("附件");

        MultiPartEmail email = new HtmlEmail();
        buildEmail(email);
        email.setSubject("测试附件邮件");
        email.setMsg("测试附件邮件");
        email.attach(attachment);
        email.attach(attachmentlocal);
        email.send();

    }

    /**
     * 发送html邮件
     *
     * @throws Exception
     * @author Alan
     * @time 2015-10-27 下午03:25:47
     */
    public static void sendHtmlMail() throws Exception {
        HtmlEmail email = new HtmlEmail();
        buildEmail(email);
        email.setSubject("测试html邮件");
        email.setTextMsg("测试html邮件");
        email.setHtmlMsg("<html>测试html邮件正文 <p>测测测测试试试试</p> </html>");
        email.send();
    }

    /**
     * 构建邮件基本信息
     *
     * @param email
     * @throws Exception
     * @author Alan
     * @time 2015-10-18 下午04:39:39
     */
    private static void buildEmail(Email email) throws Exception {
        email.setHostName("smtp.163.com");
        email.setAuthentication("ylxy3058@163.com", "lxy@3058");
        email.addTo("ylxy3058@qq.com");
        email.setFrom("ylxy3058@163.com");
        email.setCharset("UTF-8");
    }

}
