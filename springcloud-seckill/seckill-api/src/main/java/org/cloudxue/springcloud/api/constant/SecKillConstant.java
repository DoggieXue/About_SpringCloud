package org.cloudxue.springcloud.api.constant;

/**
 * @ClassName SecKillConstant
 * @Description 秒杀常量类
 * @Author xuexiao
 * @Date 2022/3/12 下午8:23
 * @Version 1.0
 **/
public class SecKillConstant {
    //订单状态：-1：无效；1：成功；2：已付款
    public static final short ORDER_INVALID = -1;
    public static final short ORDER_VALID = 1;
    public static final short ORDER_PAYED = 2;

    //秒杀的限流阈值
    public static final int MAX_ENTER = 50;
    public static final int PER_SECKILL_ENTER = 2;
}
