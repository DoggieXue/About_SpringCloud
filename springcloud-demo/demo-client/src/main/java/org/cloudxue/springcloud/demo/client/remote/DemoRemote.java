package org.cloudxue.springcloud.demo.client.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName DemoRemote
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/16 下午2:20
 * @Version 1.0
 **/
@FeignClient(name = "uaa-provider", fallback = DemoRemoteHystrix.class)
public interface DemoRemote {

    @RequestMapping(value = "/hello")
    public String hello(@RequestParam(value = "name") String name);
}
