package org.cloudxue.springcloud.standard.properties;

import lombok.Data;
import org.cloudxue.springcloud.standard.rateLimit.RedisRateLimitImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @ClassName RedisRateLimitProperties
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/3/12 下午9:22
 * @Version 1.0
 **/
@Data
@ConfigurationProperties(prefix = "application.rate.limit.redis" )
public class RedisRateLimitProperties {
    private List<RedisRateLimitImpl.LimiterInfo> limiterInfos;
}
