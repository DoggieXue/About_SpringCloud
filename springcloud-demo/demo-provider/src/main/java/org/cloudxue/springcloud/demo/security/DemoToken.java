package org.cloudxue.springcloud.demo.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

/**
 * @ClassName DemoToken
 * @Description 自动以凭证/令牌类
 * @Author xuexiao
 * @Date 2022/1/23 下午9:09
 * @Version 1.0
 **/
public class DemoToken extends AbstractAuthenticationToken {

    private String userName;
    private String password;

    public DemoToken(String username, String password) {
        super(Collections.emptyList());
        this.userName = username;
        this.password = password;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getPrincipal() {
        return userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
