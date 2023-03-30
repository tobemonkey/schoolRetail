package edu.hour.schoolretail.util.mail;

import lombok.Data;

import java.util.List;

/**
 * @Author 201926002057 戴毅
 * @Description 邮件对象
 * @Date 2023/1/30
 **/
@Data
public class BaseMessage {

    /**
     * 发件人
     */
    private String from;

    /**
     * 目的地址
     */
    private String[] target;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件附件
     */
    private List<Attachment> attachments;
}
