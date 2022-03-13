package org.cloudxue.springcloud.common.distribute.rateLimit;

/**
 * @ClassName RateLimitService
 * @Description 接口：限流服务
 * @Author xuexiao
 * @Date 2022/3/12 下午9:21
 * @Version 1.0
 **/
public interface RateLimitService {
    /**
     * 是否超过限流器
     *
     * @param cacheKey 限流的key，如：秒杀的类型和id seckill:10000
     * @return true or false
     */
    Boolean tryAcquire(String cacheKey);

    /**
     * 初始化一个限流器
     * @param type  类型
     * @param key  id
     * @param maxPermits  上限
     * @param rate  速度
     */
    void initLimitKey(String type, String key,
                      Integer maxPermits, Integer rate);
}
