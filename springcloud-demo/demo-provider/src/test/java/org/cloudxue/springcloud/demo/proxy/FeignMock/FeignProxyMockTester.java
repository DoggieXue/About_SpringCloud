package org.cloudxue.springcloud.demo.proxy.FeignMock;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.springcloud.common.result.RestOut;
import org.cloudxue.springcloud.demo.proxy.MockDemoClient;
import org.junit.Test;

/**
 * @ClassName FeignProxyMockTester
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/21 下午1:57
 * @Version 1.0
 **/
@Slf4j
public class FeignProxyMockTester {
    @Test
    public void testFeignRpc() {
        /**
         * 创建远程调用接口的本地JDK Proxy代理实例
         */
        MockDemoClient proxy = MockInvocationHandler.newInstance(MockDemoClient.class);

        RestOut<JSONObject> responseData = proxy.hello();
        log.info(responseData.toString());

        RestOut<JSONObject> echoResp = proxy.echo("proxyTest");
        log.info(echoResp.toString());
    }
}
