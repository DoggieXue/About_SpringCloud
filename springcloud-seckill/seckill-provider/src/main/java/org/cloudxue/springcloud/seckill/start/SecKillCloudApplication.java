package org.cloudxue.springcloud.seckill.start;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName SeckillCloudApplication
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/3/6 下午9:41
 * @Version 1.0
 **/
@Slf4j
@SpringBootApplication(scanBasePackages = {
        "org.cloudxue.springcloud.seckill",
        "org.cloudxue.springcloud.base",
        "org.cloudxue.springcloud.standard"
}, exclude = {SecurityAutoConfiguration.class})
@EnableSwagger2
@EnableJpaRepositories(basePackages = {
        "org.cloudxue.springcloud.seckill.dao"})
@EnableHystrix
@EntityScan(basePackages = {
        "org.cloudxue.springcloud.seckill.dao.po"
})
@EnableTransactionManagement
public class SecKillCloudApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SecKillCloudApplication.class, args);
        Environment env = applicationContext.getEnvironment();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");

        log.info("\n----------------------------------------------------------\n\t" +
                "秒杀练习服务 is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                "swagger-ui: \thttp://localhost:" + port + path + "/swagger-ui.html\n\t" +
                "actuator: \thttp://localhost:" + port + path + "/actuator/info\n\t" +
                "----------------------------------------------------------");
    }
}
