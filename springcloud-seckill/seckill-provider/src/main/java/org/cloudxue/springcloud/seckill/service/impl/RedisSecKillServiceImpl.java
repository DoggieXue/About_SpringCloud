package org.cloudxue.springcloud.seckill.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.springcloud.api.constant.SecKillConstant;
import org.cloudxue.springcloud.api.dto.SecKillDTO;
import org.cloudxue.springcloud.api.dto.SecKillOrderDTO;
import org.cloudxue.springcloud.common.exception.BusinessException;
import org.cloudxue.springcloud.common.util.UUIDUtil;
import org.cloudxue.springcloud.seckill.dao.SecKillOrderDao;
import org.cloudxue.springcloud.seckill.dao.SecKillSegmentStockDao;
import org.cloudxue.springcloud.seckill.dao.SecKillSkuDao;
import org.cloudxue.springcloud.seckill.dao.po.SecKillOrderPO;
import org.cloudxue.springcloud.standard.lock.JedisMultiSegmentLock;
import org.cloudxue.springcloud.standard.lock.RedisLockService;
import org.cloudxue.springcloud.standard.lua.ScriptHolder;
import org.cloudxue.springcloud.standard.redis.RedisRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisSeckillServiceImpl
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/3/6 下午10:03
 * @Version 1.0
 **/
@Slf4j
@Service
public class RedisSecKillServiceImpl {
    /**
     * 秒杀商品的DAO操作类
     */
    @Resource
    SecKillSkuDao secKillSkuDao;

    /**
     * 秒杀订单的DAO操作类
     */
    @Resource
    SecKillOrderDao secKillOrderDao;

    @Resource
    SecKillSegmentStockDao secKillSegmentStockDao;

    /**
     * 分布式锁
     */
    @Autowired
    RedisLockService redisLockService;

    /**
     * 缓存数据库操作类
     */
    @Resource
    RedisRepository redisRepository;

    /**
     * 获取秒杀令牌
     * @param exposedKey 商品ID
     * @param userId     用户ID
     * @return  令牌信息
     */
    public String getSecKillToken(String exposedKey, String userId) {
        String token = UUIDUtil.uuid();

        Long res = redisRepository.executeScript(
                ScriptHolder.getSeckillScript(), Collections.singletonList("setToken"),
                exposedKey,
                userId,
                token
        );

        if (res == 2) {
            throw BusinessException.builder().errMsg("秒杀商品没有找到").build();
        }
        if (res == 4) {
            throw BusinessException.builder().errMsg("库存不足,请稍后再来").build();
        }
        if (res == 5) {
            throw BusinessException.builder().errMsg("您已经排过队了").build();
        }
        if (res != 1) {
            throw BusinessException.builder().errMsg("排队失败，未知错误").build();
        }

        return token;
    }

    /**
     * 执行秒杀
     * @param inDto
     * @return
     */
    public SecKillOrderDTO executeSecKill(SecKillDTO inDto) {
        String exposedKey = inDto.getExposedKey();
        String cacheSkuId = redisRepository.getStr("seckill:sku:" + exposedKey);
        if (null == cacheSkuId) {
            throw BusinessException.builder().errMsg("秒杀未开始或已结束").build();
        }
        /**
         * 校验令牌
         */
        long skuId = Long.parseLong(cacheSkuId);
        Long userId = inDto.getUserId();

        Long res = redisRepository.executeScript(
                ScriptHolder.getSeckillScript(), Collections.singletonList("checkToken"),
                exposedKey,
                String.valueOf(userId),
                inDto.getSkillToken()
        );

        if (res != 5) {
            throw BusinessException.builder().errMsg("请提前排队").build();
        }

        SecKillOrderDTO dto = null;

        /**
         * 获取分布式锁
         */
        String lockKey = "seckill:segmentLock:" + String.valueOf(skuId);
        String requestId = redisLockService.getDefaultRequestId();
        JedisMultiSegmentLock lock = redisLockService.getSegmentLock(lockKey, requestId, redisLockService.SEGMENT_DEFAULT);
        boolean hasError = false;
        boolean locked = false;

        try {
            locked = lock.tryLock(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw BusinessException.builder().errMsg("秒杀失败").build();
        }

        if (locked) {
            //已拿到分布式锁
            try {
                //创建订单对象
                SecKillOrderPO order = SecKillOrderPO.builder().skuId(skuId).userId(userId).build();
                Date now = new Date();
                order.setCreateTime(now);
                order.setStatus(SecKillConstant.ORDER_VALID);


                //创建重复检查的订单对象
                SecKillOrderPO checkOrder = SecKillOrderPO.builder().skuId(order.getSkuId()).userId(order.getUserId()).build();

                long insertCount = secKillOrderDao.count(Example.of(checkOrder));
                if (insertCount >= 1) {
                    throw BusinessException.builder().errMsg("重复秒杀").build();
                }

                int stockLeft = (int)secKillSegmentStockDao.sumStockBySku(skuId);
                if (stockLeft <= 0) {
                    throw BusinessException.builder().errMsg("库存不够").build();
                }

                /**
                 * 进入秒杀事务
                 * 执行秒杀逻辑：1、减分段库存(不是总库存)；2、下秒杀订单
                 */
                doSecKill(order, lock);

                dto = new SecKillOrderDTO();
                BeanUtils.copyProperties(order, dto);

            } catch (Exception e) {
                e.printStackTrace();
                hasError = true;
            }

            if (!locked || hasError) {
                throw BusinessException.builder().errMsg("执行秒杀时，发生异常").build();
            }
        }

        return dto;
    }

    @Transactional
    public void doSecKill(SecKillOrderPO order, JedisMultiSegmentLock lock) {
        Long skuId = order.getSkuId();
        //插入秒杀订单
        secKillOrderDao.save(order);
        //减分段库存
        int segment = lock.getSegmentIndexLocked();
        secKillSegmentStockDao.decreaseStock(skuId, segment);
        //减库存
        secKillSkuDao.decreaseStockCountById(skuId);
    }

}
