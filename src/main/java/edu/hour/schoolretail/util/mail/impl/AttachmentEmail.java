package edu.hour.schoolretail.util.mail.impl;

import edu.hour.schoolretail.util.mail.Attachment;
import edu.hour.schoolretail.util.mail.BaseMessage;
import edu.hour.schoolretail.util.mail.MailNotice;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author 201926002057 戴毅
 * @Description 发送带附件的邮箱
 * @Date 2023/1/30
 **/
public class AttachmentEmail extends MailNotice {

    /**
     * 支持多附件发送的邮件
     *
     * @param baseMessage 内容
     * @throws MessagingException
     */
    @Override
    public void sendMail(BaseMessage baseMessage, JavaMailSender javaMailSender) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        // 要带附件第二个参数设为true
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setFrom(baseMessage.getFrom());
        helper.setTo(baseMessage.getTarget());
        helper.setSubject(baseMessage.getSubject());
        helper.setText(baseMessage.getContent(), true);

        if (baseMessage.getAttachments() != null) {
            // 添加对应的附件
            for (Attachment attachment : baseMessage.getAttachments()) {
                ByteArrayResource resource = new ByteArrayResource(attachment.getData());
                helper.addAttachment(attachment.getFilename(), resource);
            }
        }

        javaMailSender.send(message);
    }

}
