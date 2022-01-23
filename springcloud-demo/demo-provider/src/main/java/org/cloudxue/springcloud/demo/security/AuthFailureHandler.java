package org.cloudxue.springcloud.demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloudxue.springcloud.common.result.RestOut;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName AuthFailureHandler
 * @Description 认证失败处理器
 * @Author xuexiao
 * @Date 2022/1/23 下午9:35
 * @Version 1.0
 **/
public class AuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setHeader("Content-Type","text/html;charset=utf-8");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());

        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(RestOut.failed(e.getMessage()));
        httpServletResponse.getWriter().write(jsonStr);
    }
}
