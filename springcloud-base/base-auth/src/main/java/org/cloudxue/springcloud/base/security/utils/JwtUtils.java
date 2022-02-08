package org.cloudxue.springcloud.base.security.utils;

import com.alibaba.fastjson.JSONObject;
import org.cloudxue.springcloud.common.constants.SessionConstants;
import org.cloudxue.springcloud.common.util.RsaUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.interfaces.RSAPublicKey;
import java.util.stream.Collectors;

/**
 * @ClassName JwtUtils
 * @Description jwt工具类
 * @Author xuexiao
 * @Date 2022/1/25 下午4:05
 * @Version 1.0
 **/
public class JwtUtils {
    private static final String PUBKEY_START = "";
    private static final String PUBKEY_END = "";

    /**
     * 通过classpath获取公钥值
     * @return
     */
    public static RSAPublicKey getPubKeyObj() {
        Resource resource = new ClassPathResource(SessionConstants.RSA_PUBLIC_KEY);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String pubKey = br.lines().collect(Collectors.joining("\n"));
            pubKey = pubKey.substring(PUBKEY_START.length(), pubKey.indexOf(PUBKEY_END));
            return RsaUtils.getPublicKey(pubKey);
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param jwtToken token值
     * @param rsaPublicKey 公钥
     * @return
     */
    public static JSONObject decodeAndVerify (String jwtToken, RSAPublicKey rsaPublicKey) {
        SignatureVerifier rsaVerifier = new RsaVerifier(rsaPublicKey);
        Jwt jwt = JwtHelper.decodeAndVerify(jwtToken, rsaVerifier);
        return JSONObject.parseObject(jwt.getClaims());
    }

    /**
     *
     * @param jwtToken token值
     * @return
     */
    public static JSONObject decodeAndVerify(String jwtToken) {
        return decodeAndVerify(jwtToken, getPubKeyObj());
    }

    /**
     * 判断jwt是否过期
     * @param claims jwt内容
     * @param currTime 当前时间
     * @return 未过期：true;已过期：false
     */
    public static boolean checkExp (JSONObject claims, long currTime) {
        long exp = claims.getLong("exp");
        if (exp < currTime) {
            return false;
        }
        return true;
    }

    /**
     * 判断jwt是否过期
     * @param claims jwt内容
     * @return 未过期：true;已过期：false
     */
    public static boolean checkExp(JSONObject claims) {
        return checkExp(claims, System.currentTimeMillis());
    }
}