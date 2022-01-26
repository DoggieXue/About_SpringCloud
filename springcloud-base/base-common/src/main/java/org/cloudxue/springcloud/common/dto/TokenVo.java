package org.cloudxue.springcloud.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName TokenVo
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/25 下午5:12
 * @Version 1.0
 **/
@Getter
@Setter
public class TokenVo implements Serializable {
    /**
     * token值
     */
    private String tokenValue;
    /**
     * 到期时间
     */
    private Date expiration;
    /**
     * 用户名
     */
    private String username;
    /**
     * 所属应用
     */
    private String clientId;

    /**
     * 授权类型
     */
    private String grantType;
}
