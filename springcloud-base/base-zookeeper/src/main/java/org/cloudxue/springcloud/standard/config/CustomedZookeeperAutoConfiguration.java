package org.cloudxue.springcloud.standard.config;

import org.cloudxue.springcloud.common.distribute.idGenerator.IdGenerator;
import org.cloudxue.springcloud.common.distribute.rateLimit.RateLimitService;
import org.cloudxue.springcloud.distribute.idGenerator.impl.SnowflakeIdGenerator;
import org.cloudxue.springcloud.distribute.lock.LockService;
import org.cloudxue.springcloud.distribute.zookeeper.ZKClient;
import org.cloudxue.springcloud.standard.hibernate.SnowflakeIdGeneratorFactory;
import org.cloudxue.springcloud.standard.lock.impl.ZkLockServiceImpl;
import org.cloudxue.springcloud.standard.rateLimit.impl.ZkRateLimitServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @ClassName CustomedZookeeperAutoConfiguration
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/3/13 下午12:59
 * @Version 1.0
 **/
@Configuration
@ConditionalOnProperty(prefix = "zookeeper", name = "address" )
public class CustomedZookeeperAutoConfiguration {
    @Value("${zookeeper.address}" )
    private String zkAddress;

    /**
     * 自定义的ZK客户端bean
     *
     * @return
     */
    @Bean(name = "zKClient" )
    public ZKClient zKClient() {
        return new ZKClient(zkAddress);
    }

    /**
     * 获取 ZK 限流器的 bean
     */
    @Bean
    @DependsOn("zKClient" )
    public RateLimitService zkRateLimitServiceImpl() {
        return new ZkRateLimitServiceImpl();
    }

    /**
     * 获取 ZK 分布式锁的 bean
     */

    @Bean
    @DependsOn("zKClient" )
    public LockService zkLockServiceImpl() {
        return new ZkLockServiceImpl();
    }


    /**
     * 获取通用的分布式ID 生成器 工程
     */
    @Bean
    @DependsOn("zKClient" )
    public SnowflakeIdGeneratorFactory snowflakeIdGeneratorFactory() {
        return new SnowflakeIdGeneratorFactory();
    }


    /**
     * 获取秒杀商品的分布式ID 生成器
     */
    @Bean
    @DependsOn("zKClient" )
    public IdGenerator seckillSkuIdentityGenerator() {
        return new SnowflakeIdGenerator("seckillSkuIdentityGenerator" );
    }


    /**
     * 获取秒杀订单的分布式ID 生成器
     */
    @Bean
    @DependsOn("zKClient" )
    public IdGenerator seckillOrderIdentityGenerator() {
        return new SnowflakeIdGenerator("seckillOrderIdentityGenerator" );
    }
}
