package org.cloudxue.springcloud.demo.proxy.basic;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.springcloud.demo.proxy.MockDemoClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @ClassName DemoClientInvocationHandler
 * @Description 动态代理RPC调用处理器
 * @Author xuexiao
 * @Date 2022/1/19 下午4:01
 * @Version 1.0
 **/
@Slf4j
public class DemoClientInvocationHandler implements InvocationHandler {
    /**
     * 被代理的委托类实例
     */
    private MockDemoClient realClient;

    public DemoClientInvocationHandler(MockDemoClient realClient) {
        this.realClient = realClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        log.info("{} 方法被调用", name);

        /**
         * 直接调用hello方法
         */
        if (name.equals("hello")) {
            return realClient.hello();
        }
        /**
         * 通过反射调用echo方法
         */
        if (name.equals("echo")) {
            return method.invoke(realClient, args);
        }
        /**
         * 通过反射调用其他的方法
         */
        Object result = method.invoke(realClient, args);
        return result;
    }
}
