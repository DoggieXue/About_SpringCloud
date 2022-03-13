package org.cloudxue.springcloud.common.util;

import java.util.UUID;

/**
 * @ClassName UUIDUtil
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/3/12 下午9:30
 * @Version 1.0
 **/
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
