package org.cloudxue.springcloud.user.info;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @ClassName UAACloudApplication
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/16 上午9:43
 * @Version 1.0
 **/

@EnableDiscoveryClient
//@EnableEurekaClient
@SpringBootApplication
@Slf4j
public class UAACloudApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(UAACloudApplication.class,args);
        Environment env = applicationContext.getEnvironment();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        String ip = env.getProperty("eureka.instance.ip-address");

        log.info("\n----------------------------------------------------------\n\t" +
                "UAA 用户账号与认证服务 is running! Access URLs:\n\t" +
                "Local: \t\thttp://"+ ip+":"+ port +  path + "/\n\t" +
                "------------------------------------------------------");
    }
}
