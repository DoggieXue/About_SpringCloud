package org.cloudxue.springcloud.demo.start;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName DemoCloudApplication
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/15 下午6:26
 * @Version 1.0
 **/
@EnableAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class,
        ManagementWebSecurityAutoConfiguration.class
})
@EnableSwagger2
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {
        "org.cloudxue.springcloud.demo",
        "org.cloudxue.springcloud.demo.client.fallback",
        "org.cloudxue.springcloud.standard"
}, exclude = {SecurityAutoConfiguration.class})
@Slf4j
public class DemoCloudApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(DemoCloudApplication.class, args);
        /**
         * 打印所有的Spring IOC Bean
         */
//        List<String> beans = SpringContextUtil.getBeanDefinitionNames();
//        log.info(beans.toString());

        Environment environment = applicationContext.getEnvironment();
        String port = environment.getProperty("server.port");
        String name = environment.getProperty("spring.application.name");
        String path = environment.getProperty("server.servlet.context-path");

        if (StringUtils.isBlank(path)) {
            path = "";
        }

        String ip = environment.getProperty("spring.cloud.client.ip-address");
        log.info("\n----------------------------------------------------------\n\t" +
                name.toUpperCase() + " is running! Access URLs:\n\t" +
                "Local: \t\thttp://" + ip + ":" + port + path + "/\n\t" +
                "swagger-ui: \thttp://" + ip + ":" + port + path + "/swagger-ui.html\n\t" +
                "actuator: \thttp://" + ip + ":" + port + path + "/actuator/info\n\t" +
                "----------------------------------------------------------");

    }
}
