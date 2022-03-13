package org.cloudxue.springcloud.seckill.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @ClassName SecKillSegmentStockPO
 * @Description 秒杀商品的库存
 * @Author xuexiao
 * @Date 2022/3/6 下午10:19
 * @Version 1.0
 **/
@Entity
@Table(name = "SECKILL_SEGMENT_STOCK")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecKillSegmentStockPO {
    @Id
    @GenericGenerator(
            name = "snowflakeIdGenerator",
            strategy = "org.cloudxue.springcloud.standard.hibernate.CommonSnowflakeIdGenerator"
    )
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "snowflakeIdGenerator")
    @Column(name = "SKU_STOCK_ID", unique = true, nullable = false, length = 8)
    private Long id;

    //商品ID
    @Column(name = "SKU_ID")
    private Long skuId;

    //分段编号 从0开始
    @Column(name = "SEG_INDEX")
    private Integer segmentIndex;

    //剩余库存数量
    @Column(name = "STOCK_COUNT")
    private long stockCount;

    //原始库存库量
    @Column(name = "RAW_STOCK")
    private long rawStockCount;
}
