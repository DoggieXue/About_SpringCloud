package org.cloudxue.springcloud.demo.proxy.FeignMock;

/**
 * @ClassName RpcMethodHandler
 * @Description 模拟Feign的方法处理器MethodHandler
 * @Author xuexiao
 * @Date 2022/1/20 上午10:22
 * @Version 1.0
 **/
public interface RpcMethodHandler {
    /**
     * 功能：组装URL、完成REST RPC远程调用、返回JSON结果
     * @param argv RPC 方法参数
     * @return REST返回结果
     * @throws Throwable
     */
    Object invoke(Object[] argv) throws Throwable;
}
