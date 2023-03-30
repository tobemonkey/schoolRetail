package edu.hour.schoolretail.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

/**
 * MD5 生成工具
 */
@Slf4j
public class MD5Utils {


    private static final String SALT = "PjKqlwZSGn1v/H4Vq3bw1g=="; // 固定的盐值
/*
    *//**
     * 获取盐的初值
     *//*
    static {
        Properties properties = null;
        try {
            InputStream inputStream = new FileInputStream("application.properties");
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            log.error("application.properties 文件读取异常！{}", e.getMessage());
        }
        SALT = properties.getProperty("spring.utils.md5.salt");
    }*/

    /**
     *
     * @param input
     * @return
     */
    public static String generateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String inputWithSalt = input + SALT; // 在字符串和盐值之间拼接固定字符串
            md.update(inputWithSalt.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
