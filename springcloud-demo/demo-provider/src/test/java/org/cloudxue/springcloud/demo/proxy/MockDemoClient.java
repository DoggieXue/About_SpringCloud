package org.cloudxue.springcloud.demo.proxy;

import com.alibaba.fastjson.JSONObject;
import org.cloudxue.springcloud.common.result.RestOut;
import org.cloudxue.springcloud.demo.constant.TestConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MockDemoClient
 * @Description 模拟Feign远程接口调用
 * @Author xuexiao
 * @Date 2022/1/19 上午10:54
 * @Version 1.0
 **/
@RestController(value = TestConstants.DEMO_CLIENT_PATH)
public interface MockDemoClient {

    /**
     * 远程调用接口的方法
     * 调用 demo-provider 的 REST接口： api/demo/hello/v1
     * REST 接口 功能：返回 hello world
     * @return
     */
    @GetMapping(name = "api/demo/hello/v1")
    RestOut<JSONObject> hello();

    /**
     * 远程调用接口的方法
     * 调用 demo-provider 的 REST接口： api/demo/echo/{word}/v1
     * REST 接口 功能：返回 回显输入的信息
     * @param word
     * @return
     */
    @GetMapping(name = "api/demo/echo/{0}/v1")
    RestOut<JSONObject> echo(String word);
}
