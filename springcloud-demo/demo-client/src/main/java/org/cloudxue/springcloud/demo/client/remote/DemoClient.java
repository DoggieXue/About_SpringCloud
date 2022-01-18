package org.cloudxue.springcloud.demo.client.remote;

import com.alibaba.fastjson.JSONObject;
import org.cloudxue.springcloud.common.result.RestOut;
import org.cloudxue.springcloud.demo.client.fallback.DemoClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName DemoClient
 * @Description 远程服务的本地声明式调用
 * @Author xuexiao
 * @Date 2022/1/18 上午10:10
 * @Version 1.0
 **/
@FeignClient(
        value = "demo-provider",
        path = "/demo-provider/api/demo/",
//        fallback = DemoDefaultFallback.class
        fallbackFactory = DemoClientFallbackFactory.class
)

public interface DemoClient {
    /**
     * 远程调用接口方法
     * 调用 demo-provider 的REST接口： api/demo/hello/v1
     * REST接口功能：返回 hello world
     * @return JSON响应实例
     */
    @GetMapping("/hello/v1")
    RestOut<JSONObject> hello();

    /**
     * 远程调用接口方法
     * 调用 demo-provider 的REST接口： api/demo/echo/{word}/v1
     * REST接口功能：回显输入的信息
     * @param word
     * @return 回显JSON响应实例
     */
    @RequestMapping(value = "/echo/{word}/v1", method = RequestMethod.GET)
    RestOut<JSONObject> echo(@PathVariable(value = "word") String word);
}
