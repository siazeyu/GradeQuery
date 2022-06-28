package szy.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * @author Siaze
 * @date 2022/6/28
 */
public class MailUtil {

    private static final String MAIL_HOST = "smtp.exmail.qq.com"; // 发送邮件的主机

    private static final String FROM = "*****"; // 发件人邮箱地址

    /**
     * 用qq邮箱发送一个简单邮件
     *
     * @param text
     * @param toRecipients 接收邮件，逗号分隔
     * @throws AddressException
     * @throws MessagingException
     */
    public static void sentSimpleMail( String text, String toRecipients)
            throws AddressException, MessagingException {
        /*
         * 初始化JavaMail会话
         */
        Properties props = System.getProperties(); // 获得系统属性配置，用于连接邮件服务器的参数配置
        props.setProperty("mail.smtp.host", MAIL_HOST); // 发送邮件的主机
        props.setProperty("mail.smtp.auth", "true");

        Session session = Session.getInstance(props, null);// 获得Session对象
        session.setDebug(true); // 设置是否显示debug信息,true 会在控制台显示相关信息

        /*
         * 创建邮件消息，发送邮件
         */
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM));

        // To: 收件人
        message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(toRecipients, false));

        message.setSubject("成绩更新通知"); // 邮件标题

        MimeMultipart mimeMultipart = new MimeMultipart();
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(text, "text/html;charset=utf-8");
        mimeMultipart.addBodyPart(mimeBodyPart);
        message.setContent(mimeMultipart);

        // 简单发送邮件的方式
        Transport.send(message, FROM, "password"); // 授权码
    }

}
