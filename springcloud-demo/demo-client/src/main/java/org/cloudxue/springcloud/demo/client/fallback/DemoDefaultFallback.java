package org.cloudxue.springcloud.demo.client.fallback;

import com.alibaba.fastjson.JSONObject;
import org.cloudxue.springcloud.common.result.RestOut;
import org.cloudxue.springcloud.demo.client.remote.DemoClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName DemoDefaultFallback
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/18 上午10:14
 * @Version 1.0
 **/
@Component
public class DemoDefaultFallback implements DemoClient {


    @Override
    @GetMapping("/hello/v1")
    public RestOut<JSONObject> hello() {
        return RestOut.failed("远程调用失败,返回熔断后的调用结果");
    }

    @Override
    public RestOut<JSONObject> echo(String word) {
        return RestOut.failed("远程调用失败,返回熔断后的调用结果");
    }
}
