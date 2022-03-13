package org.cloudxue.springcloud.seckill.dao;

import org.cloudxue.springcloud.seckill.dao.po.SecKillSkuPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @ClassName SecKillSkuDao
 * @Description 秒杀商品DAO操作
 * @Author xuexiao
 * @Date 2022/3/12 下午7:51
 * @Version 1.0
 **/
@Repository
public interface SecKillSkuDao extends JpaRepository<SecKillSkuPO, Long>, JpaSpecificationExecutor<SecKillSkuPO> {
    /**
     * 减库存
     * @param id
     * @return
     */
    @Transactional
    @Modifying
    @Query("update SecKillSkuPO t set t.stockCount = t.stockCount - 1 where t.id = :id")
    int decreaseStockCountById(@Param("id") Long id);
}
