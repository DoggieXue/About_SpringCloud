package org.cloudxue.springcloud.demo.client.fallback;

import com.alibaba.fastjson.JSONObject;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.springcloud.common.result.RestOut;
import org.cloudxue.springcloud.demo.client.remote.DemoClient;
import org.springframework.stereotype.Component;

/**
 * @ClassName DemoClientFallbackFactory
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/18 下午10:00
 * @Version 1.0
 **/

@Slf4j
@Component
public class DemoClientFallbackFactory implements FallbackFactory<DemoClient> {

    /**
     * 创建DemoClient客户端的回退处理实例
     * @param cause
     * @return
     */
    @Override
    public DemoClient create(final Throwable cause) {
        log.error("RPC异常了，回退！", cause);
        /**
         * 创建一个客户端接口的匿名回退实例
         */
        return new DemoClient() {
            @Override
            public RestOut<JSONObject> hello() {
                return RestOut.failed("FallbackFactory fallback：测试/hello/v1 服务调用失败" );
            }

            @Override
            public RestOut<JSONObject> echo(String word) {
                return RestOut.failed("FallbackFactory fallback: /echo/"+ word +"/v1 服务调用失败");
            }
        };
    }
}
