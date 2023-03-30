package edu.hour.schoolretail;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

/**
 * @Author 201926002057 戴毅
 * @Description
 * @Date 2023/2/12
 **/
public class RegexTest {

    private static final String PASSWORD_PATTERN = "^[a-zA-Z0-9]{10,16}$";

    private static final String EMAIL_PATTERN = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    @Test
    public void emailTest() {
        System.out.println(Pattern.matches(EMAIL_PATTERN, "2403160038@qq.com"));
    }

    @Test
    public void passwordTest() {
        System.out.println(Pattern.matches(PASSWORD_PATTERN, "20020327qq"));
    }
}
