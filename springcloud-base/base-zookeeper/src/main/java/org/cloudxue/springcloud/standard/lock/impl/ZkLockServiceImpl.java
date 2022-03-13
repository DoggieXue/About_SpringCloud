package org.cloudxue.springcloud.standard.lock.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.cloudxue.springcloud.distribute.lock.LockService;
import org.cloudxue.springcloud.distribute.zookeeper.ZKClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName ZkLockServiceImpl
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/3/13 下午1:05
 * @Version 1.0
 **/
public class ZkLockServiceImpl implements LockService {
    Map<String, InterProcessMutex> lockMap = new ConcurrentHashMap<>();


    /**
     * 取得ZK 的分布式锁
     *
     * @param key 锁的key
     * @return ZK 的分布式锁
     */
    public InterProcessMutex getZookeeperLock(String key)
    {
        CuratorFramework client = ZKClient.getSingleton().getClient();
        InterProcessMutex lock = lockMap.get(key);
        if (null == lock)
        {
            lock = new InterProcessMutex(client, "/mutex/seckill/" + key);
            lockMap.put(key, lock);
        }
        return lock;
    }
}
