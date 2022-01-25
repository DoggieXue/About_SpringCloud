package org.cloudxue.springcloud.standard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ClassName DefaultPasswordConfig
 * @Description 全局加密器IOC容器实例
 * @Author xuexiao
 * @Date 2022/1/25 上午11:15
 * @Version 1.0
 **/
@Configuration
public class DefaultPasswordConfig {
    /**
     * 装配一个全局的Bean，用于密码加密和匹配
     * @return 返回 BCryptPasswordEncoder 加密器实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
