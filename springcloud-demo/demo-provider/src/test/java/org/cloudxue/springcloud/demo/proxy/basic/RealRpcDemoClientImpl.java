package org.cloudxue.springcloud.demo.proxy.basic;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.springcloud.common.result.RestOut;
import org.cloudxue.springcloud.common.util.HttpRequestUtil;
import org.cloudxue.springcloud.common.util.JsonUtil;
import org.cloudxue.springcloud.demo.constant.TestConstants;
import org.cloudxue.springcloud.demo.proxy.MockDemoClient;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * @ClassName RealRpcDemoClientImpl
 * @Description 简单的RPC客户端实现类
 * 主要工作如下：
 * 1、组装REST接口URL
 * 2、通过HttpClient组件调用REST接口并获得响应结果
 * 3、接续REST接口的响应结果，封装成JSON对象，并返回给调用者
 * @Author xuexiao
 * @Date 2022/1/19 上午11:03
 * @Version 1.0
 **/
@Slf4j
public class RealRpcDemoClientImpl implements MockDemoClient {

    final String contextPath = TestConstants.DEMO_CLIENT_PATH;

    @Override
    public RestOut<JSONObject> hello() {
        /**
         * 远程调用接口方法：完成demo-provider的REST API远程调用
         * REST API  功能：返回hello world
         */
        String uri = "api/demo/hello/v1";
        //组装 REST 接口 URL
        String restUrl = contextPath + uri;
        log.info("restUrl={}", restUrl);

        //通过HttpClient组件调用REST接口
        String responseData = null;
        try {
            responseData = HttpRequestUtil.simpleGet(restUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //解析 REST 接口的响应结果，解析成 JSON对象，并且返回
        RestOut<JSONObject> result = JsonUtil.jsonToPojo(responseData, new TypeReference<RestOut<JSONObject>>(){});

        return result;
    }

    @Override
    public RestOut<JSONObject> echo(String word) {
        /**
         * 远程调用接口方法：完成demo-provider的REST API远程调用
         * REST API  功能：回显输入信息
         */
        String uri = "api/demo/echo/{0}/v1";

        String restUrl = contextPath + MessageFormat.format(uri, word);
        log.info("restUrl={}", restUrl);

        String responseData = null;
        try {
            responseData = HttpRequestUtil.simpleGet(restUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        RestOut<JSONObject> result = JsonUtil.jsonToPojo(responseData, new TypeReference<RestOut<JSONObject>>(){});

        return result;
    }
}
