package org.cloudxue.springcloud.seckill.dao.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName SecKillOrderPO
 * @Description 秒杀订单PO 秒杀订单表
 * @Author xuexiao
 * @Date 2022/3/6 下午10:15
 * @Version 1.0
 **/
@Entity
@Table(name = "SECKILL_ORDER")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecKillOrderPO implements Serializable {
    //订单ID
    @Id
    @GenericGenerator(
            name = "snowflakeIdGenerator",
            strategy = "org.cloudxue.springcloud.standard.hibernate.CommonSnowflakeIdGenerator"
    )
    @GeneratedValue(strategy= GenerationType.IDENTITY, generator = "snowflakeIdGenerator")
    @Column(name = "ORDER_ID", unique = true,nullable = false,length = 8)
    private Long id;

    //用户ID
    @Column(name="USER_ID")
    private Long userId;
    //商品ID
    @Column(name = "SKU_ID")
    private Long skuId;
    //支付金额
    @Column(name = "PAY_MONEY")
    private BigDecimal money;
    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "CREATE_TIME")
    private Date createTime;
    //支付时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "PAY_TIME")
    private Date payTime;
    //订单状态
    @Column(name = "STATUS")
    private Short status;
}
