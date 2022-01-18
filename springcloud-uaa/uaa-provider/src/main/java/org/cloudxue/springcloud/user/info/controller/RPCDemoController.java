package org.cloudxue.springcloud.user.info.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.springcloud.common.result.RestOut;
import org.cloudxue.springcloud.demo.client.remote.DemoClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName RPCDemoController
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/16 上午9:44
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/api/call/demo/")
@Api(tags = "演示 demo-provider 远程调用")
public class RPCDemoController {

    /**
     * 注入@FeignClient 注解配置 所配置的demo-provider 客户端Feign实例
     */
    @Resource
    DemoClient demoClient;

    /**
     * 调用 demo-provider的 REST接口： api/demo/hello/v1
     * @return
     */
    @GetMapping("/hello/v1")
    @ApiOperation(value = "hello 远程调用")
    public RestOut<JSONObject> remoteHello() {
        log.info("方法 remoteHello 被调用了");

        RestOut<JSONObject> result = demoClient.hello();
        JSONObject data = new JSONObject();
        data.put("demo-data", result);
        return RestOut.success(data).setRespMsg("操作成功");
    }

    /**
     * 调用 demo-provider 的 REST接口： api/demo/echo/{word}/v1
     * @param word
     * @return
     */
    @GetMapping("/echo/{word}/v1")
    @ApiOperation(value = "echo 远程调用")
    public RestOut<JSONObject> remoteEcho (@PathVariable(value = "word") String word) {
        log.info("方法 remoteEcho 被调用了");
        RestOut<JSONObject> result = demoClient.echo(word);
        JSONObject data = new JSONObject();
        data.put("demo-data", result);
        return RestOut.success(data).setRespMsg("操作成功");
    }
}
