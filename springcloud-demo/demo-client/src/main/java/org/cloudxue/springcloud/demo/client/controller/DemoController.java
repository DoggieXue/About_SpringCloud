package org.cloudxue.springcloud.demo.client.controller;

import org.cloudxue.springcloud.demo.client.remote.DemoRemote;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName DemoController
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/16 下午2:19
 * @Version 1.0
 **/
@RestController
public class DemoController {

//    使用Autowired注解后，编译不通过，需要配合Qualifier注解来指定实现类，
//    但是会直接执行熔断，而不会进行RPC远程调用
//    此处使用Resource注解来处理
//@Qualifier("demoRemoteHystrix")
//@Autowired
    @Resource
    DemoRemote demoRemote;

    @RequestMapping("/hello/{name}")
    public String index(@PathVariable(value = "name") String name) {
        return demoRemote.hello(name);
    }
}
