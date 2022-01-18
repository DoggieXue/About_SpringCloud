package org.cloudxue.springcloud.standard.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.cloudxue.springcloud.common.constants.SessionConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName TokenFeignConfiguration
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/18 下午2:49
 * @Version 1.0
 **/
@Configuration
public class TokenFeignConfiguration implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(null==attributes) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();

        String token = request.getHeader(SessionConstants.AUTHORIZATION_HEAD);

        if (null != token) {
            requestTemplate.header(SessionConstants.AUTHORIZATION_HEAD, new String[]{token});
        }

        String sessionSeed = request.getHeader(SessionConstants.USER_IDENTIFIER);
        if (null != sessionSeed) {
            requestTemplate.header(SessionConstants.USER_IDENTIFIER, new String[]{sessionSeed});
        }
    }
}
