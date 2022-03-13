package org.cloudxue.springcloud.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName SecKillSkuDTO
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/3/12 下午8:30
 * @Version 1.0
 **/
@Data
public class SecKillSkuDTO {
    //商品ID
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    //商品标题
    private String title;
    //商品价格
    private BigDecimal price;
    //秒杀价格
    private BigDecimal costPrice;
    //秒杀结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
    private Date createTime;
    //秒杀结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
    private Date startTime;
    //秒杀结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
    private Date endTime;

    @ApiModelProperty(value = "剩余库存", example = "10000")
    private Integer stockCount;
    @ApiModelProperty(value = "原始库存", example = "10000")
    private Integer rawStockCount;

    //是否开启秒杀
    private boolean exposed = false;

    //秒杀MD5
    //加密措施，避免用户通过抓包拿到秒杀地址
    @ApiModelProperty(value = "秒杀MD5", example = "1212121")
    private String exposedKey;
}
