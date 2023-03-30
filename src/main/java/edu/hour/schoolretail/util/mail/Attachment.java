package edu.hour.schoolretail.util.mail;

import lombok.Data;

/**
 * @Author 201926002057 戴毅
 * @Description 附件
 * @Date 2023/1/30
 **/
@Data
public class Attachment {

    private String filename;

    private byte[] data;
}
