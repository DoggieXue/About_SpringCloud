package org.cloudxue.springcloud.demo.provider.controller;

import com.alibaba.fastjson.JSONObject;
import org.cloudxue.springcloud.common.result.RestOut;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @ClassName DemoController
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/18 上午10:50
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api/demo")
public class DemoController {

    @GetMapping("/hello/v1")
    public RestOut<JSONObject> hello() {
        JSONObject data = new JSONObject();
        data.put("hello", "world");
        return RestOut.success(data).setRespMsg("操作成功");
    }

    @GetMapping("/echo/{word}/v1")
    public RestOut<JSONObject> echo(@PathVariable(value = "word") String word) {
        JSONObject data = new JSONObject();
        data.put("echo", word);
        return RestOut.success(data).setRespMsg("操作成功");
    }

    /**
     * 获取HTTP头部信息实例
     * @param request
     * @return
     */
    @GetMapping("/header/echo/v1")
    public RestOut<JSONObject> echo(HttpServletRequest request) {
        JSONObject data = new JSONObject();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String)headerNames.nextElement();
            String value = request.getHeader(key);
            data.put(key, value);
        }
        return RestOut.success(data).setRespMsg("操作成功");
    }
}
