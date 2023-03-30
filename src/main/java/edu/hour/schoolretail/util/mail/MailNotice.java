package edu.hour.schoolretail.util.mail;

import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;

/**
 * @Author 201926002057 戴毅
 * @Description
 * @Date 2023/1/30
 **/
public abstract class MailNotice {

    /**
     * 发送邮件
     *
     * @param message 内容
     */
    public abstract void sendMail(BaseMessage message, JavaMailSender javaMailSender) throws MessagingException;
}
