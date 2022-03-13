package org.cloudxue.springcloud.standard.rateLimit.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.RetryNTimes;
import org.cloudxue.springcloud.common.distribute.rateLimit.RateLimitService;
import org.cloudxue.springcloud.distribute.zookeeper.ZKClient;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName ZkRateLimitServiceImpl
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/3/13 下午1:01
 * @Version 1.0
 **/
public class ZkRateLimitServiceImpl implements RateLimitService {
    Map<String, DistributedAtomicInteger> counterMap = new LinkedHashMap<>();
    //秒杀的限流阈值
    public static final int MAX_ENTER = 50;

    /**
     * 取得ZK的 分布式计数器
     *
     * @param key 锁的key
     * @return ZK 的分布式计数器
     */
    public Boolean tryAcquire(String key) {
        CuratorFramework client = ZKClient.getSingleton().getClient();
        String path = "/counter/seckill/" + key;
        DistributedAtomicInteger counter = counterMap.get(key);
        if (null == counter) {
            counter = new DistributedAtomicInteger(client, path, new RetryNTimes(3, 5000));
            counterMap.put(key, counter);
        }

        try {
            if (counter.get().preValue() <= MAX_ENTER) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 创建一个限流的key
     *
     * @param type       类型
     * @param key        id
     * @param maxPermits 上限
     * @param rate       速度
     */
    @Override
    public void initLimitKey(String type, String key, Integer maxPermits, Integer rate){

    }
}
