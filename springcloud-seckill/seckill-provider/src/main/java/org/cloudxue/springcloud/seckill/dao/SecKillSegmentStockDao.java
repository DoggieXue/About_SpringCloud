package org.cloudxue.springcloud.seckill.dao;

import org.cloudxue.springcloud.seckill.dao.po.SecKillSegmentStockPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @ClassName SecKillSegmentStockDao
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/3/12 下午7:56
 * @Version 1.0
 **/
@Repository
public interface SecKillSegmentStockDao extends JpaRepository<SecKillSegmentStockPO, Long>, JpaSpecificationExecutor<SecKillSegmentStockPO> {

    @Transactional
    @Modifying
    @Query("update SecKillSegmentStockPO t set t.stockCount = t.stockCount - 1 where t.skuId = :skuId and t.segmentIndex = :segmentIndex")
    int decreaseStock(@Param("skuId") Long skuId, @Param("segmentIndex") Integer segmentIndex);

    @Transactional
    @Modifying
    @Query("delete from SecKillSegmentStockPO t where t.skuId = :skuId")
    int deleteStockBySku(@Param("skuId") Long skuId);

    @Query("select sum(t.stockCount) from SecKillSegmentStockPO t where t.skuId = :skuId")
    int sumStockBySku(@Param("skuId") Long skuId);
}
