package org.cloudxue.springcloud.standard.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.cloudxue.springcloud.common.constants.SessionConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName FeignConfiguration
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/18 上午11:32
 * @Version 1.0
 **/
@Configuration
public class FeignConfiguration implements RequestInterceptor {
    /**
     * 配置RPC请求时的头部参数
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == attributes) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();

        String token = request.getHeader(SessionConstants.AUTHORIZATION_HEAD);
        if (null != token) {
            token = StringUtils.removeStart(token, "Bearer");
            /**
             * 设置令牌
             */
            requestTemplate.header(SessionConstants.AUTHORIZATION_HEAD, new String[]{token});
        }

        String userIdentifier = request.getHeader(SessionConstants.USER_IDENTIFIER);
        if (null != userIdentifier)
        {
            requestTemplate.header(SessionConstants.USER_IDENTIFIER, new String[]{userIdentifier});
        }
    }
}
