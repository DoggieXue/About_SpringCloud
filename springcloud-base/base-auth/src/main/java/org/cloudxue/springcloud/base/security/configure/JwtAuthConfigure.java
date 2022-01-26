package org.cloudxue.springcloud.base.security.configure;

import org.cloudxue.springcloud.base.security.filter.JwtAuthenticationFilter;
import org.cloudxue.springcloud.base.security.handler.AuthFailureHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * @ClassName JwtAuthConfigure
 * @Description HTTP安全认证配置类，将JwtAuthenticationFilter加入过滤处理责任链
 * @Author xuexiao
 * @Date 2022/1/25 下午4:00
 * @Version 1.0
 **/
public class JwtAuthConfigure<T extends  JwtAuthConfigure<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public JwtAuthConfigure() {
        this.jwtAuthenticationFilter = new JwtAuthenticationFilter();
    }

    /**
     * 将过滤器加入到请求过滤处理责任链
     * @param builder
     * @throws Exception
     */
    @Override
    public void configure(B builder) throws Exception {
        //获取SpringSecurity共享的 AuthenticationManager 认证提供者实例
        //设置到 jwtAuthenticationFilter 认证过滤器
        jwtAuthenticationFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        jwtAuthenticationFilter.setAuthenticationFailureHandler(new AuthFailureHandler());

        JwtAuthenticationFilter filter = postProcess(jwtAuthenticationFilter);
        //将过滤器加入到 http 过滤处理责任链
        builder.addFilterBefore(filter, LogoutFilter.class);
    }

    public JwtAuthConfigure<T, B> permissiveRequestUrls(String... urls)
    {
        jwtAuthenticationFilter.setPermissiveUrl(urls);

        return this;
    }

    public JwtAuthConfigure<T, B> tokenValidSuccessHandler(AuthenticationSuccessHandler successHandler)
    {
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
        return this;
    }
}
