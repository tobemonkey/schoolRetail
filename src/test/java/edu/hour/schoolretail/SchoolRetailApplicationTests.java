package edu.hour.schoolretail;

import edu.hour.schoolretail.util.EmailUtil;
import edu.hour.schoolretail.util.mail.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.mail.MessagingException;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Slf4j
class SchoolRetailApplicationTests {

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private ApplicationContext context;


    @Test
    void plainTextEmailTest() {

        try {
            emailUtil.sendPlainTextMail("测试内容", "测试主题", new String[]{"2403160039@qq.com"});
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void attachmentEmailTest() throws IOException {

        List<Attachment> attachments = new ArrayList<>();
        Attachment a1 = new Attachment();
        File file = new File("C:\\Users\\admin\\Desktop\\周报2.xlsx");
        InputStream inputStream = Files.newInputStream(file.toPath());
        a1.setFilename("测试文件1.xlsx");
        byte[] bytes = toByteArray(inputStream);
        a1.setData(bytes);
        attachments.add(a1);

        Attachment a2 = new Attachment();
        file = new File("C:\\Users\\admin\\Desktop\\代码.zip");
        inputStream = Files.newInputStream(file.toPath());
        a2.setFilename("yig.zip");
        bytes = toByteArray(inputStream);
        a2.setData(bytes);
        attachments.add(a2);

        try {
            emailUtil.sendAttachmentEmail("测试内容", "测试主题", attachments, new String[]{"2403160039@qq.com"});
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void verifyCodeEmail() {
        try {
            emailUtil.sendVerifyCode("2403160039@qq.com");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }


    @Test
    public void beanSelect() {
        String[] beans = context.getBeanDefinitionNames();
        Arrays.sort(beans);
        for (String bean : beans) {
            log.info(bean);
        }
    }
}
