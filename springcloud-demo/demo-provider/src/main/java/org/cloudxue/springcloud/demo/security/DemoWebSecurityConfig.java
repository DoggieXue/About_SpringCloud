package org.cloudxue.springcloud.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @ClassName DemoWebSecurityConfigure
 * @Description Spring Security的安全配置类：对WEB容器的HTTP安全认证机制进行配置
 *              1、应用HTTP安全认证配置类：DemoAuthConfigure
 *              2、构造AuthenticationManagerBuilder认证管理者实例。
 * @Author xuexiao
 * @Date 2022/1/23 下午10:17
 * @Version 1.0
 **/
//开启WEB容器的HTTP安全认证机制
//@EnableWebSecurity
public class DemoWebSecurityConfig extends WebSecurityConfigurerAdapter {
    //Demo 1：Spring Security基本流程演示
    @Autowired
    private DemoAuthProvider demoAuthProvider;

    /**
     * 配置HTTP请求的安全策略，应用DemoAuthConfigure配置类实例
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //CSRF禁用,基于Token
        http.csrf().disable()
                .authorizeRequests()
                // swagger start
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-ui.html/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/configuration/ui").permitAll()
                .antMatchers("/configuration/security").permitAll()
                .antMatchers("/**/favicon.ico","/**/css/**", "/**/js/**").permitAll()
                // swagger end
                //其他所有请求需要认证
                .anyRequest().authenticated()
                .and()
                //取消HTTP基本认证
                .httpBasic().disable()
                //取消表单登录认证
                .formLogin().disable()
                //禁用session
                .sessionManagement().disable()
                .cors()
                .and()
                //应用自定义的DemoAuthConfigure配置类
                .apply(new DemoAuthConfigure<>());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/**/api/user/login/v1",
                "/**/v2/api-docs",
                "/**/swagger-resources/configuration/ui",
                "/**/swagger-resources",
                "/**/swagger-resources/configuration/security",
                "/images/**",
                "/swagger-ui.html",
                "/webjars/**",
                "/**/favicon.ico",
                "/**/css/**",
                "/**/js/**"
        );
    }

    /**
     * 配置认证Builder，由其负责构造AuthenticationManager认证管理者实例
     * Builder将构造认证管理者实例，并且作为HTTP请求的共享对象存储在代码中
     * 可以通过http.getSharedObject(AuthenticationManager.class)来获取管理者实例
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //Demo 1，基本演示流程：加入自定义的Provider认证提供者实例
        auth.authenticationProvider(demoAuthProvider);
    }
}
