package edu.hour.schoolretail.util.mail.impl;

import edu.hour.schoolretail.util.mail.BaseMessage;
import edu.hour.schoolretail.util.mail.MailNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


/**
 * @Author 201926002057 戴毅
 * @Description 纯文本邮件
 * @Date 2023/1/30
 **/
public class PlainTextEmail extends MailNotice {

    @Override
    public void sendMail(BaseMessage baseMessage, JavaMailSender javaMailSender) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(baseMessage.getFrom());
        message.setTo(baseMessage.getTarget());
        message.setSubject(baseMessage.getSubject());
        message.setText(baseMessage.getContent());

        javaMailSender.send(message);
    }
}
