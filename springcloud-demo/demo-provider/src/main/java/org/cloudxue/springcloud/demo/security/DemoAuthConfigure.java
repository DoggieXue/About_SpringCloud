package org.cloudxue.springcloud.demo.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * @ClassName DemoAuthConfigure
 * @Description HTTP的安全认证配置类：将过滤器加入到过滤处理责任链
 * @Author xuexiao
 * @Date 2022/1/23 下午10:06
 * @Version 1.0
 **/
public class DemoAuthConfigure<T extends DemoAuthConfigure<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {
    /**
     * 创建认证过滤器
     */
    private DemoAuthFilter authFilter = new DemoAuthFilter();

    /**
     * 将过滤器加入http过滤处理责任链
     * @param builder
     * @throws Exception
     */
    @Override
    public void configure(B builder) throws Exception {
        /**
         * 获取Spring Security共享的AuthenticationManager认证管理者实例，将其设置到认证过滤器
         */
        authFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));

        DemoAuthFilter filter = postProcess(authFilter);
        //将过滤器加入到http过滤处理责任链
        builder.addFilterBefore(filter, LogoutFilter.class);
    }
}
