package edu.hour.schoolretail.util;

import edu.hour.schoolretail.util.mail.Attachment;
import edu.hour.schoolretail.util.mail.BaseMessage;
import edu.hour.schoolretail.util.mail.MailNotice;
import edu.hour.schoolretail.util.mail.impl.AttachmentEmail;
import edu.hour.schoolretail.util.mail.impl.PlainTextEmail;
import edu.hour.schoolretail.util.mail.impl.VerifyCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author 201926002057 戴毅
 * @Description 发送邮件
 * @Date 2023/1/30
 **/
@Component
@Slf4j
public class EmailUtil {

    private static final String VERIFY_CODE_SUBJECT = "验证码";

    private static final String EMAIL_PATTERN = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    @Value("${mail.fromMail.fromAddress}")
    private String username;

    @Value("${mail.verify.length}")
    private int length;

    @Resource
    private JavaMailSenderImpl javaMailSender;

    @Resource
    private TemplateEngine templateEngine;

    /**
     * 发送纯文本邮件
     *
     * @param content 内容
     * @param subject 主题
     * @param target  接收方邮箱
     */
    public void sendPlainTextMail(String content, String subject,
                                  String[] target) throws MessagingException {

        // 检测是否初始化mailSender对象
        if (checkInitialize()) {
            throw new IllegalArgumentException("邮件发送初始化异常!");
        }
        MailNotice email = new PlainTextEmail();

        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setFrom(username);
        baseMessage.setTarget(target);
        baseMessage.setContent(content);
        baseMessage.setSubject(subject);

        email.sendMail(baseMessage, javaMailSender);
    }



    /**
     * 发送验证码
     * @param target 目标邮箱
     */
    public String sendVerifyCode(String target) throws MessagingException {
        // 生成验证码
        String verifyCode = VerifyCodeGenerator.generateVerificationCode(length);
        // 补充模板正文
        Context context = new Context();
        context.setVariable("verifyCode", verifyCode.toCharArray());
        String template = templateEngine.process("common/verifycode/verifyTemplate", context);
        // todo 需要放开
        sendAttachmentEmail(template, VERIFY_CODE_SUBJECT, null, new String[]{target});

        return verifyCode;
    }


    /**
     * 发送带附件的邮件
     *
     * @param content     内容
     * @param subject     主题
     * @param attachments 附件
     * @param target      接收方邮箱
     * @throws MessagingException
     */
    public void sendAttachmentEmail(String content, String subject,
                                    List<Attachment> attachments, String[] target) throws MessagingException {

        if (checkInitialize()) {
            throw new IllegalArgumentException("邮件发送初始化异常!");
        }
        MailNotice email = new AttachmentEmail();

        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setFrom(username);
        baseMessage.setTarget(target);
        baseMessage.setContent(content);
        baseMessage.setSubject(subject);
        baseMessage.setAttachments(attachments);

        email.sendMail(baseMessage, javaMailSender);
    }

    /**
     * 检查邮件发送对象是否为空
     */
    private boolean checkInitialize() {
        if (javaMailSender == null) {
            log.error("邮件发送初始化失败！");
            return true;
        }
        return false;
    }


    /**
     * 判断传入的 email 是否合规
     * @param email
     * @return
     */
    public boolean isLegalEmail(String email) throws NullPointerException {
        if (email == null) {
            throw new NullPointerException("传入邮箱不能为空！");
        }
        return Pattern.matches(EMAIL_PATTERN, email);
    }
}
