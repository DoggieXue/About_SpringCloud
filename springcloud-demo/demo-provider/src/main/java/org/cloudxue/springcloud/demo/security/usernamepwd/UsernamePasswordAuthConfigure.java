package org.cloudxue.springcloud.demo.security.usernamepwd;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * @ClassName UsernamePasswordAuthConfigure
 * @Description:
 * @Author: Doggie
 * @Date: 2023年02月17日 17:42:58
 * @Version 1.0
 **/
public class UsernamePasswordAuthConfigure<T extends UsernamePasswordAuthConfigure<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {
    /**
     * 创建自定义的用户名密码认证过滤器
     */
    private UsernamePasswordAuthFilter authFilter = new UsernamePasswordAuthFilter();

    @Override
    public void configure(B builder) throws Exception {
        authFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        UsernamePasswordAuthFilter filter = postProcess(authFilter);
        builder.addFilterBefore(filter, LogoutFilter.class);
    }
}
