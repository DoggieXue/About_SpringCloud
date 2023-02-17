package org.cloudxue.springcloud.demo.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName DemoAuthProvider
 * @Description 自定义凭证提供者类，与自定义凭证DemoToken配套，完整对DemoToken的认证
 * @Author xuexiao
 * @Date 2022/1/23 下午9:20
 * @Version 1.0
 **/
@Service
public class DemoAuthProvider implements AuthenticationProvider {

    public DemoAuthProvider() {

    }

    /**
     * 模拟的数据源，实际从DB中获取
     */
    private Map<String, String> map = new LinkedHashMap<>();{
        //初始化模拟的数据源，放入两个用户
        map.put("dog","123456");
        map.put("cat","123456");
    }

    /**
     * 具体的凭证验证方法
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        DemoToken token = (DemoToken)authentication;
        //获取数据源中的密码
        String rawPass = map.get(token.getUserName());
        //校验密码，若不相等，抛出异常
        if (!token.getPassword().equals(rawPass)) {
            token.setAuthenticated(false);
            throw new BadCredentialsException("认证有误：令牌校验失败！");
        }
        token.setAuthenticated(true);
        return token;
    }

    /**
     * 判断凭证是否被支持
     * @param authentication 此处仅DemoToken凭证被支持
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(DemoToken.class);
    }
}
