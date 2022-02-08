package org.cloudxue.springcloud.standard.hibernate;

import org.cloudxue.springcloud.common.distribute.idGenerator.IdGenerator;
import org.cloudxue.springcloud.distribute.idGenerator.impl.SnowflakeIdGenerator;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IncrementGenerator;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName CommonSnowflakeIdGenerator
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/2/7 下午4:08
 * @Version 1.0
 **/
public class CommonSnowflakeIdGenerator extends IncrementGenerator {
    /**
     * 生成器的 map 缓存
     * key 为 PO 类名,value 为分布式ID生成器
     */
    private static Map<String, SnowflakeIdGenerator> generatorMap = new LinkedHashMap<>();



    /**
     * 从父类继承方法：生成分布式 ID
     */
    @Override
    public Serializable generate(
            SharedSessionContractImplementor sessionImplementor, Object object)
            throws HibernateException {

        /**
         * 以 PO 的类名，作为 ID 的类型
         * 每一个类型对应一个 分布式 ID 生成器
         */
        String type = object.getClass().getSimpleName();

        Serializable id = null;

        /**
         * 从 map 中取得分布式 ID 生成器
         */
        IdGenerator idGenerator = getFromMap(type);
        /**
         * 调用自定义的  Zookeeper + Snowflake 算法生成 ID
         */
        id = idGenerator.nextId();
        if (null != id) {
            return id;
        }

        /**
         * 如果生成失败，则通过父类生成
         */
        id = sessionImplementor.getEntityPersister(null, object)
                .getClassMetadata().getIdentifier(object, sessionImplementor);
        return id != null ? id : super.generate(sessionImplementor, object);
    }

    /**
     * 从 map 中获取缓存的分布式 ID 生成器，没有则创建一个
     *
     * @param type 生成器的绑定类型，为 PO 类名
     * @return 分布式 ID 生成器
     */
    public static synchronized IdGenerator getFromMap(String type) {
        if (generatorMap.containsKey(type)) {
            return generatorMap.get(type);
        }

        /**
         * 创建分布式 ID 生成器，并且存入 map
         */
        SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(type);
        generatorMap.put(type, idGenerator);
        return idGenerator;
    }
}
