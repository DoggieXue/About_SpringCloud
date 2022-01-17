package org.cloudxue.springcloud.demo.client.controller;

import org.cloudxue.springcloud.demo.client.remote.DemoRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName DemoController
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/16 下午2:19
 * @Version 1.0
 **/
@RestController
public class DemoController {
    @Autowired
    DemoRemote demoRemote;

    @RequestMapping("/hello/{name}")
    public String index(@PathVariable(value = "name") String name) {
        return demoRemote.hello(name);
    }
}
