package org.cloudxue.springcloud.center.zuul.config;

import org.cloudxue.springcloud.base.security.configure.JwtAuthConfigure;
import org.cloudxue.springcloud.base.security.handler.JwtRefreshSuccessHandler;
import org.cloudxue.springcloud.base.security.provider.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;

import javax.annotation.Resource;

/**
 * @ClassName ZuulWebSecurityConfig
 * @Description Spring Security安全配置类
 *              对Web容器的HTTP安全认证机制进行配置
 *              在Zuul网关微服务中，将应用JwtAuthenConfigure配置实例，构造AuthenticationManagerBuilder认证管理者实例
 * @Author xuexiao
 * @Date 2022/1/26 上午10:51
 * @Version 1.0
 **/
@EnableWebSecurity
public class ZuulWebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
//                .anyRequest().permitAll()
                .antMatchers(
                        "/**/api/user/login/v1",
                        "/**/actuator/hystrix",
                        "/**/validate/code/**",
                        "/**/validate/**",
                        "/**/oauth/**",
                        "/**/css/**",
                        "/**/images/**",
                        "/**/js/**",
                        "/**/users-anon/**",
                        "/**/login.html",
                        "/**/api/session/login/v1",
                        "/**/oauth/user/token",
                        "/**/actuator/hystrix.stream",
                        "/**/api/mock/**",
                        "/**/v2/api-docs",
                        "/**/swagger-resources/configuration/ui",
                        "/**/swagger-resources",
                        "/**/swagger-resources/configuration/security",
                        "/**/swagger-ui.html",
                        "/**/css/**",
                        "/**/js/**",
                        "/**/api/seckill/**",
                        "/blog/**",
                        "/**/images/**",
                        "/**/webjars/**",
                        "/seckill-provider/api/seckill/seglock/getSeckillResult/v1",
                        "/seckill-provider/api/crazymaker/rockmq/sendSeckill/v1",
                        "/seckill-provider/api/seckill/order/**",
                        "/demo-provider/api/demo/header/echo/v1",
                        "/demo-provider/api/demo/hello/v1",
                        "/demo-provider/api/demo/hello/v1",
                        "/**/favicon.ico",
                        "/ZuulFilter/demo"
                ).permitAll()
                .antMatchers("/image/**").permitAll()
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin().disable()
                .sessionManagement().disable()
                .cors()
                .and()
//                .headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
//                new Header("Access-control-Allow-Origin", "*"),
//                new Header("Access-Control-Expose-Headers", SessionConstants.AUTHORIZATION_HEAD))))
//                .and()
//                .addFilterBefore(sessionSeedFilter, CorsFilter.class)
//                .addFilterAfter(new OptionsRequestFilter(), CorsFilter.class)
                .apply(new JwtAuthConfigure<>()).tokenValidSuccessHandler(jwtRefreshSuccessHandler()).permissiveRequestUrls("/logout")
                .and()
                .logout().disable()
//                .addFilterBefore(new SessionDataLoadFilter(), SessionManagementFilter.class)
                .sessionManagement().disable();
//    		.sessionManagement().maximumSessions(1)
//                  .maxSessionsPreventsLogin(false)
//                  .expiredUrl("/login?expired")
//			      .sessionRegistry(sessionRegistry());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/**/api/user/login/v1",
                "/**/actuator/hystrix",
                "/**/validata/code/**",
                "/**/validate/**",
                "/**/oauth/**",
                "/**/css/**",
                "/**/images/**",
                "/**/js/**",
                "/**/users-anon/**",
                "/**/login.html",
                "/**/api/session/login/v1",
                "/**/oauth/user/token",
                "/**/actuator/hystrix.stream",
                "/**/api/mock/**",
                "/**/v2/api-docs",
                "/**/swagger-resources/configuration/ui",
                "/**/swagger-resources",
                "/**/swagger-resources/configuration/security",
                "/**/swagger-ui.html",
                "/**/css/**",
                "/**/js/**",
                "/**/images/**",
                "/**/webjars/**",
                "/**/api/seckill/**",
                "/blog/**",
                "/seckill-provider/api/seckill/seglock/getSeckillResult/v1",
                "/seckill-provider/api/crazymaker/rockmq/sendSeckill/v1",
                "/seckill-provider/api/seckill/order/**",
                "/demo-provider/api/demo/header/echo/v1",
                "/demo-provider/api/demo/hello/v1",
                "/**/favicon.ico",
                "/ZuulFilter/demo"
        );
    }

    /**
     * 配置认证构造者，由其负责构造AuthenticationManager认证管理者实例
     * 所构造的AuthenticationManager实例，会作为 HTTP 请求共享对象
     * 可以通过http.getSharedObject(AuthenticationManager.class)来获取
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthenticationProvider());
    }

    /**
     * 注入session存储实例，用于查找session
     */
    @Resource
    RedisOperationsSessionRepository sessionRepository;

    @DependsOn("sessionRepository")
    @Bean("jwtAuthenticationProvider")
    protected AuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(sessionRepository);
    }

    @Bean
    protected JwtRefreshSuccessHandler jwtRefreshSuccessHandler() {
        return new JwtRefreshSuccessHandler();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
