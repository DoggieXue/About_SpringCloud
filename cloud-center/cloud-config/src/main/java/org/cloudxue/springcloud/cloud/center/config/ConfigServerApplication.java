package org.cloudxue.springcloud.cloud.center.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @ClassName ConfigServerApplication
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/14 上午10:32
 * @Version 1.0
 **/
@SpringBootApplication
@EnableConfigServer  //开启配置服务器的支持
@EnableEurekaClient  //开启Eureka客户端的支持
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class);
    }
}
