package org.cloudxue.springcloud.standard.lock;

import org.cloudxue.springcloud.common.exception.BusinessException;
import org.cloudxue.springcloud.standard.context.SpringContextUtil;
import org.cloudxue.springcloud.standard.lua.ScriptHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName InnerLock
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/3/12 下午9:08
 * @Version 1.0
 **/
public class InnerLock {
    private RedisTemplate redisTemplate;

    public static final Long LOCKED = Long.valueOf(1);
    public static final Long UNLOCKED = Long.valueOf(1);
    public static final int EXPIRE = 2000;

    String key;
    String requestId;  // lockValue 锁的value ,代表线程的uuid

    /**
     * 默认为2000ms
     */
    long expire = 2000L;

    private volatile boolean isLocked = false;
    private RedisScript lockScript;
    private RedisScript unLockScript;


    public InnerLock(String lockKey, String requestId) {
        this.key = lockKey;
        this.requestId = requestId;
        lockScript = ScriptHolder.getLockScript();
        unLockScript = ScriptHolder.getUnlockScript();
    }

    /**
     * 抢夺锁
     */
    public void lock() {
        if (null == key) {
            return;
        }
        try {
            List<String> redisKeys = new ArrayList<>();
            redisKeys.add(key);
            redisKeys.add(requestId);
            redisKeys.add(String.valueOf(expire));

            Long res = (Long) getRedisTemplate().execute(lockScript, redisKeys);
            isLocked = false;
        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.builder().errMsg("抢锁失败").build();
        }
    }

    /**
     * 有返回值的抢夺锁
     *
     * @param millisToWait
     */
    public boolean lock(Long millisToWait) {
        if (null == key) {
            return false;
        }
        try {
            List<String> redisKeys = new ArrayList<>();
            redisKeys.add(key);
            redisKeys.add(requestId);
            redisKeys.add(String.valueOf(millisToWait));
            Long res = (Long) getRedisTemplate().execute(lockScript, redisKeys);

            return res != null && res.equals(LOCKED);
        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.builder().errMsg("抢锁失败").build();
        }

    }

    //释放锁
    public void unlock() {
        if (key == null || requestId == null) {
            return;
        }
        try {
            List<String> redisKeys = new ArrayList<>();
            redisKeys.add(key);
            redisKeys.add(requestId);
            Long res = (Long) getRedisTemplate().execute(unLockScript, redisKeys);
        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.builder().errMsg("释放锁失败").build();

        }
    }

    private RedisTemplate getRedisTemplate() {
        if(null==redisTemplate) {
            redisTemplate= (RedisTemplate) SpringContextUtil.getBean("stringRedisTemplate");
        }
        return redisTemplate;
    }
}
