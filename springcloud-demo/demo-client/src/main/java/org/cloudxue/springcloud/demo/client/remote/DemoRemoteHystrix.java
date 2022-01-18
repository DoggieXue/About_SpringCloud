package org.cloudxue.springcloud.demo.client.remote;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName DemoRemoteHystrix
 * @Description Hystrix回调类
 * @Author xuexiao
 * @Date 2022/1/17 下午3:46
 * @Version 1.0
 **/
@Component
public class DemoRemoteHystrix implements DemoRemote{

    @Override
    public String hello(@RequestParam(value = "name") String name) {
        return "hello " + name + ", this message send failed";
    }
}
