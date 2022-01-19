package org.cloudxue.springcloud.demo.proxy.basic;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.springcloud.common.result.RestOut;
import org.cloudxue.springcloud.demo.client.remote.DemoClient;
import org.cloudxue.springcloud.demo.proxy.MockDemoClient;
import org.junit.Test;

/**
 * @ClassName ProxyTester
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/19 上午11:26
 * @Version 1.0
 **/
@Slf4j
public class ProxyTester {
    /**
     * 不用代理，进行简单的RPC调用
     */
    @Test
    public void simpleRPCTest() {
        MockDemoClient realObject = new RealRpcDemoClientImpl();

        RestOut<JSONObject> helloResult = realObject.hello();
        log.info("helloResult={}", helloResult);

        RestOut<JSONObject> echoResult = realObject.echo("回显内容");
        log.info("echoResult={}", echoResult);
    }

    /**
     * 静态代理测试
     */
    @Test
    public void staticProxyTest() {
        /**
         * 被代理的真实RPC调用类
         */
        MockDemoClient realObject = new RealRpcDemoClientImpl();
        /**
         * 静态的代理类
         */
        DemoClient proxy = new DemoClientStaticProxy(realObject);

        RestOut<JSONObject> helloResult = proxy.hello();
        log.info("helloResult={}", helloResult);

        RestOut<JSONObject> echoResult = proxy.echo("staticProxy");
        log.info("echoResult={}", echoResult);
    }
}