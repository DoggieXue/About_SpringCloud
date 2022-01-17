package org.cloudxue.springcloud.user.info.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName RPCDemoController
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/16 上午9:44
 * @Version 1.0
 **/
@Slf4j
@RestController
@Api(tags = "演示 demo-provider 远程调用")
public class RPCDemoController {

    @RequestMapping("/hello")
    public String index(@RequestParam String name) {
        return "Hello " + name + ", Welcome to Spring Cloud World, I'm Produce 2";
    }
}
