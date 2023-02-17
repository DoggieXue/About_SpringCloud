package org.cloudxue.springcloud.demo.security;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.springcloud.common.constants.SessionConstants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName DemoAuthFilter
 * @Description 安全认证的过滤器类：
 * 从请求中获取用户信息并组装成凭证类，交给认证管理者。
 * 生产场景中，通常从HTTP头部信息获取（如Cookie、Token等）
 * @Author xuexiao
 * @Date 2022/1/23 下午9:30
 * @Version 1.0
 **/
@Slf4j
public class DemoAuthFilter extends OncePerRequestFilter {
    //认证失败处理器
    private AuthenticationFailureHandler failureHandler = new AuthFailureHandler();

    /**
     * 判断认证头是否存在，不存在直接跳过
     */
    private RequestMatcher requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher(SessionConstants.AUTHORIZATION_HEAD);

    /**
     * 认证管理者，是认证方法的入口，接收一个Authentication对象
     * ProviderManager 是 AuthenticationManager的一个实现类
     */
    private AuthenticationManager authenticationManager;

    @Override
    public void afterPropertiesSet() throws ServletException {
        Assert.notNull(failureHandler, "AuthenticationFailureHandler must be specified!");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /**
         * 处理掉不需要过滤的请求：请求头不包含token字段的请求
         */
        if (!requireAuthentication(request)) {
            filterChain.doFilter(request,response);
            return;
        }

        AuthenticationException failed = null;
        try {
            Authentication returnToken = null;
            boolean success = false;

            String token = request.getHeader(SessionConstants.AUTHORIZATION_HEAD);
            if (StringUtils.isEmpty(token)) {
                failed = new AuthenticationServiceException("认证失败：token值为空！");
            } else {
                String[] parts = token.split(",");
                //Demo1: 认证演示
                DemoToken demoToken = new DemoToken(parts[0], parts[1]);
                returnToken = this.authenticationManager.authenticate(demoToken);
                success = demoToken.isAuthenticated();

                if (success) {
                    //认证成功，设置上下文凭证
                    SecurityContextHolder.getContext().setAuthentication(returnToken);
                    //执行后续操作
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        } catch (Exception e) {
            logger.error("认证有误", e);
            failed = new AuthenticationServiceException("请求头认证消息格式有误", e);
        }
        if (null == failed) {
            failed = new AuthenticationServiceException("认证失败");
        }
        //认证失败了
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request,response,failed);
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    protected boolean requireAuthentication(HttpServletRequest request) {
        return requiresAuthenticationRequestMatcher.matches(request);
    }
}
