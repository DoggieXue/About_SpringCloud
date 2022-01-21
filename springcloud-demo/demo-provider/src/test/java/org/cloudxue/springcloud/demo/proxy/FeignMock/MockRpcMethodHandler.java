package org.cloudxue.springcloud.demo.proxy.FeignMock;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.springcloud.common.result.RestOut;
import org.cloudxue.springcloud.common.util.HttpRequestUtil;
import org.cloudxue.springcloud.common.util.JsonUtil;

import java.text.MessageFormat;

/**
 * @ClassName MockRpcMethodHandler
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/20 下午3:40
 * @Version 1.0
 **/
@Slf4j
public class MockRpcMethodHandler implements RpcMethodHandler{
    /**
     * REST URL 的前面部分，用于远程调用 Feign 接口的类级别注解
     */
    final String contextPath;
    /**
     * REST URL 的前面部分，用于远程调用 Feign 接口的方法级别注解
     */
    final String url;

    public MockRpcMethodHandler(String contextPath, String url) {
        this.contextPath = contextPath;
        this.url = url;
    }

    /**
     * 功能：
     * 组装URL
     * 完成REST RPC 远程调用
     * 返回JSON结果
     *
     * @param argv RPC 方法参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object[] argv) throws Throwable {
        /**
         * 组装URL
         */
        String requestUrl = contextPath + MessageFormat.format(url, argv);
        log.info("=======requestUrl = {}", requestUrl);
        /**
         * 完成REST RPC 远程调用
         */
        String responseData = HttpRequestUtil.simpleGet(requestUrl);
        /**
         * 解析 REST 接口的响应结果，解析成 JSON 对象
         */
        RestOut<JSONObject> result = JsonUtil.jsonToPojo(responseData, new TypeReference<RestOut<JSONObject>>(){});

        return result;
    }
}
