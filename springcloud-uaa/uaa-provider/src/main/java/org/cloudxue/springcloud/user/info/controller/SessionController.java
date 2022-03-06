package org.cloudxue.springcloud.user.info.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cloudxue.springcloud.base.service.impl.UserServiceImpl;
import org.cloudxue.springcloud.common.constants.SessionConstants;
import org.cloudxue.springcloud.common.exception.BusinessException;
import org.cloudxue.springcloud.common.result.RestOut;
import org.cloudxue.springcloud.user.info.api.dto.LoginInfoDTO;
import org.cloudxue.springcloud.user.info.api.dto.LoginOutDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName SessionController
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/26 下午2:03
 * @Version 1.0
 **/
@Api(value = "用户端登录与登出", tags = {"用户端登录与登出"})
@RestController
@RequestMapping("/api/session")
@Slf4j
public class SessionController {
    /**
     * 用户端会话服务
     */
    @Resource
    private UserServiceImpl userService;

    @PostMapping("/login/v1")
    @ApiOperation(value = "用户端登录")
    public RestOut<LoginOutDTO> login (@RequestBody LoginInfoDTO loginInfoDTO,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        log.info("当前请求URL： " + request.getRequestURL());
        LoginOutDTO dto = userService.login(loginInfoDTO);
        setResponseHeader(response, dto.getToken());
        return RestOut.success(dto);
    }

    @PostMapping("/token/refresh/v1")
    @ApiOperation(value = "前台刷新token")
    public RestOut<LoginOutDTO> tokenRefresh(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(SessionConstants.AUTHORIZATION_HEAD);
        if (StringUtils.isEmpty(token)) {
            throw BusinessException.builder().errMsg("刷新失败").build();
        }

        LoginOutDTO outDTO = userService.tokenRefresh(token);
        setResponseHeader(response, outDTO.getToken());
        return RestOut.success(outDTO);
    }

    private static void setResponseHeader(HttpServletResponse response,String token) {
        response.setHeader("Content-Type","text/html;charset=utf-8");
        response.setHeader(SessionConstants.AUTHORIZATION_HEAD, token);
    }
}
