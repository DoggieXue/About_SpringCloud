package org.cloudxue.springcloud.distribute.lock;

import org.apache.curator.framework.recipes.locks.InterProcessMutex;

public interface LockService {
    /**
     * 取得ZK 的分布式锁
     *
     * @param key 锁的key
     * @return ZK 的分布式锁
     */
    InterProcessMutex getZookeeperLock(String key);
}
