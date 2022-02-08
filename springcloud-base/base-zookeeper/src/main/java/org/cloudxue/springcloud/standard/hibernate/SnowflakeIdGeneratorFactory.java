package org.cloudxue.springcloud.standard.hibernate;

import org.cloudxue.springcloud.common.distribute.idGenerator.IdGenerator;
import org.cloudxue.springcloud.distribute.idGenerator.impl.SnowflakeIdGenerator;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName SnowflakeIdGeneratorFactory
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/2/7 下午4:08
 * @Version 1.0
 **/
public class SnowflakeIdGeneratorFactory {
    private Map<String, SnowflakeIdGenerator> generatorMap;

    public SnowflakeIdGeneratorFactory() {
        generatorMap = new LinkedHashMap<>();
    }

    public synchronized IdGenerator getIdGenerator(String type) {
        if (generatorMap.containsKey(type)) {
            return generatorMap.get(type);
        }

        SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(type);
        generatorMap.put(type, idGenerator);
        return idGenerator;
    }
}
