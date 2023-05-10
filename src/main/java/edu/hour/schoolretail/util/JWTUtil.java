package edu.hour.schoolretail.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 201926002057 戴毅
 * @Description jwt 验证工具
 * @Date 2023/2/7
 **/
@Slf4j
public class JWTUtil {

    // token 的盐
    private static final String SECRET = "*TEUw%JIew893UIUert";

    // token 超时时间
    private static final Integer EXPIRE = 18;

    private static final JWTCreator.Builder builder = JWT.create();

    // 设置 token 签名算法
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    /**
     * 通过传入的信息创建 token
     * @param id 用户id
     * @param role 用户给身份
     * @param expire 过期时间
     * @param filed 时间单位
     * @return 返回 token
     */
    public static String createToken(Long id, Integer role, int expire, int filed) {
        // 设置令牌过期时间为指定天数后
        Calendar instance = Calendar.getInstance();
        instance.add(filed, expire);
        // 添加负载，
        return builder.withClaim("id", id)
                .withClaim("role", role)
                .withExpiresAt(instance.getTime())
                .sign(algorithm);
    }

    /**
     * 通过信息获取 token
     * @param id 需要添加进 token 的信息
     * @param role
     * @return
     */
    public static String createToken(Long id, Integer role) {

        // 设置令牌过期时间为指定天数后
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.HOUR, EXPIRE);
        // 添加负载，
        return builder.withClaim("id", id)
                .withClaim("role", role)
                .withExpiresAt(instance.getTime())
                .sign(algorithm);
    }


    /**
     * 检测 token，如果成功则返回 token 信息，否则抛出异常
     * @param token
     * @return
     * @throws Exception
     */
    public static DecodedJWT decodeToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        try {
            return JWT.require(algorithm)
                    .build()
                    .verify(token);
        } catch (TokenExpiredException e) {
            log.info("token 超时！token 为：{}，错误信息为：{}", token, e.getMessage());
            throw new TokenExpiredException("token解析异常：token 超时");
        } catch (SignatureVerificationException e) {
            log.info("签名异常！token 为：{}，错误信息为：{}", token, e.getMessage());
            throw new IllegalArgumentException("token解析异常：token 签名出错", e);
        } catch (AlgorithmMismatchException e) {
            log.info("算法不匹配！token 为：{}，错误信息为：{}", token, e.getMessage());
            throw new IllegalArgumentException("token解析异常：token 解析算法出错", e);
        } catch (Exception e) {
            log.error("其他异常！token 为：{}，错误信息为：{}", token, e);
            throw e;
        }

    }


    /**
     * 获取 token 解析后的数据
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, String> getClaimsMap(String token) {
        Map<String, String> map = new HashMap<>();
        DecodedJWT decode = null;
        try {
            decode = decodeToken(token);
        } catch (Exception e) {
            log.error("token 解析异常：{}", e.getMessage());
            throw e;
        }
        map.put("id", String.valueOf(decode.getClaim("id").asLong()));
        map.put("role", String.valueOf(decode.getClaim("role").asInt()));
        map.put("exp", String.valueOf(decode.getClaim("exp").asDate()));
        return map;
    }

    /**
     * 通过 token 获取 id
     * @param token
     * @return
     * @throws Exception
     */
    public static Long getUserIdByToken(String token) throws IllegalArgumentException, Exception  {
        return Long.valueOf(getClaimsMap(token).get("id"));
    }

    /**
     * 通过 token 获取用户属性
     * @param token
     * @return
     * @throws Exception
     */
    public static Integer getRole(String token) throws IllegalArgumentException, Exception  {
        return Integer.valueOf(getClaimsMap(token).get("role"));
    }
}
