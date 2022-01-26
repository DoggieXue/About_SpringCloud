package org.cloudxue.springcloud.base.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloudxue.springcloud.common.result.RestOut;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName AuthFailureHandler
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/25 下午11:11
 * @Version 1.0
 **/
public class AuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        response.setHeader("Content-Type", "text/html;charset=utf-8" );
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ObjectMapper mapper = new ObjectMapper();

        //Result对象转Json,
        String jsonValue = mapper.writeValueAsString(RestOut.failed(e.getMessage()));
        response.getWriter().write(jsonValue);
    }
}
