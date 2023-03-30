package edu.hour.schoolretail.utils;

import com.alibaba.druid.filter.config.ConfigTools;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JasyptUtil {

    public static void main(String[] args) throws Exception {


        String[] keyPair = new String[0];
        try {
            keyPair = ConfigTools.genKeyPair(512);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        //私钥
        String privateKey = keyPair[0];
        //公钥
        String publicKey = keyPair[1];
        System.out.println("privateKey:" + privateKey);
        System.out.println("publicKey:" + publicKey);

        //密码明文
        List<String> passwords = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            passwords.add(sc.nextLine());
        }

        String password;
        //用私钥加密后的密文
        for (String s : passwords) {
            password = ConfigTools.encrypt(privateKey, s);
            System.out.println(s + ": " + password);
        }
    }


    private String decrypt(String publicKey, String password) throws Exception {
        return ConfigTools.decrypt(publicKey, password);
    }
}