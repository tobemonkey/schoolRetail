package edu.hour.schoolretail.util.mail.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @Author 201926002057 戴毅
 * @Description
 * @Date 2023/2/1
 **/
@Component
public class VerifyCodeGenerator {

    private static final String[] META_CODE={"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};


    /**
     * 生成长度为 length 的验证码，验证码包括数字和大写字母
     * @param length 验证码长度
     * @return 验证码
     */
    public static String generateVerificationCode(int length) {
        Random random = new Random();
        StringBuilder verificationCode = new StringBuilder();
        while (verificationCode.length() < length){
            int i = random.nextInt(META_CODE.length);
            verificationCode.append(META_CODE[i]);
        }
        return verificationCode.toString();
    }
}
