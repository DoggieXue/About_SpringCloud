package org.cloudxue.springcloud.demo.proxy.basic;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.springcloud.common.result.RestOut;
import org.cloudxue.springcloud.demo.client.remote.DemoClient;
import org.cloudxue.springcloud.demo.proxy.MockDemoClient;

/**
 * @ClassName DemoClientStaticProxy
 * @Description 静态代理类
 * 1、远程抽象接口：MockClient.java
 * 2、真实被委托类：RealRpcDemoClientImpl.java
 * 3、静态代理类：DemoClientStaticProxy.java
 * 4、远程接口：DemoClient.java
 * @Author xuexiao
 * @Date 2022/1/19 下午3:13
 * @Version 1.0
 **/
@Slf4j
@AllArgsConstructor //全参构造注解
public class DemoClientStaticProxy implements DemoClient {

    /**
     * 被代理的真正实例
     */
    private MockDemoClient realClient;

    @Override
    public RestOut<JSONObject> hello() {
        log.info("静态代理：hello方法被调用");
        return realClient.hello();
    }

    @Override
    public RestOut<JSONObject> echo(String word) {
        log.info("静态代理：echo方法被调用");
        return realClient.echo(word);
    }
}
