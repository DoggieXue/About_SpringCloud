package org.cloudxue.springcloud.web.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * @ClassName SeckillWebapplication
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/3/6 下午9:12
 * @Version 1.0
 **/
@SpringBootApplication(scanBasePackages = {}, exclude = {SecurityAutoConfiguration.class})
public class SeckillWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillWebApplication.class, args);
    }
}
