package org.cloudxue.springcloud.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName SecKillSegmentDTO
 * @Description 秒杀DTO
 * @Author xuexiao
 * @Date 2022/3/12 下午8:39
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecKillDTO implements Serializable {
    //秒杀用户的用户ID
    @ApiModelProperty(value = "用户ID",example = "1")
    private Long userId;
    //秒杀商品
    @ApiModelProperty(value = "秒杀商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long skuId;
    //秒杀令牌
    @ApiModelProperty(value = "秒杀令牌")
    private String skillToken;
    //暴露地址
    @ApiModelProperty(value = "暴露地址")
    private String exposedKey;

    //秒杀库存(修改库存时用到)
    @ApiModelProperty(value = "秒杀库存", example = "10000")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer newStockNum;
}
