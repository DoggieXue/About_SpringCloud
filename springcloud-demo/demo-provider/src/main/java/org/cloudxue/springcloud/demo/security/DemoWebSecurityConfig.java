package org.cloudxue.springcloud.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @ClassName DemoWebSecurityConfigure
 * @Description Spring Security的安全配置类
 *              对WEB容器的HTTP安全认证机制进行配置
 * @Author xuexiao
 * @Date 2022/1/23 下午10:17
 * @Version 1.0
 **/
@EnableWebSecurity
public class DemoWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean("demoAuthProvider")
    protected DemoAuthProvider demoAuthProvider() {
        return new DemoAuthProvider();
    }

    /**
     * 配置HTTP请求的安全策略，应用DemoAuthConfigure配置类实例
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //CSRF禁用,基于Token，禁用session
        http.csrf().disable().sessionManagement().disable()
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
                // swagger end
                //其他所有请求需要认证
                .anyRequest().authenticated()
                .and()
                .apply(new DemoAuthConfigure<>());
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
        //加入自定义的Provider认证提供者实例
        auth.authenticationProvider(demoAuthProvider());
    }
}
