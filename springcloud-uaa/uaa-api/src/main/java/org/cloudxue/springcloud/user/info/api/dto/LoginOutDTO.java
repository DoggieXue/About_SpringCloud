package org.cloudxue.springcloud.user.info.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginOutDTO {
    private Long userId;

    private String username;

    private String nickname;

    private String token;
    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
    private Date createTime;
    //更新时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
    private Date updateTime;

    private String headImgUrl;

    private String mobile;

    private Integer sex;

    private Boolean enabled;

    private String type;

    private String openId;

    private boolean idDel;
}
