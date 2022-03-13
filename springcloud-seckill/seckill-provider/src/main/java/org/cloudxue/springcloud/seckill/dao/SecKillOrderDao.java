package org.cloudxue.springcloud.seckill.dao;

import org.cloudxue.springcloud.seckill.dao.po.SecKillOrderPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @ClassName SecKillOrderDao
 * @Description 秒杀商品订单操作DAO
 * @Author xuexiao
 * @Date 2022/3/6 下午10:14
 * @Version 1.0
 **/
@Repository
public interface SecKillOrderDao extends JpaRepository<SecKillOrderPO, Long>, JpaSpecificationExecutor<SecKillOrderPO> {

}
