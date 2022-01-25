package org.cloudxue.springcloud.demo.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
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
 * 生产场景中，通常从HTTP头部信息获取（如Cookie、Token等   ）
 * @Author xuexiao
 * @Date 2022/1/23 下午9:30
 * @Version 1.0
 **/
@Slf4j
public class DemoAuthFilter extends OncePerRequestFilter {

    public static final String USER_INFO = "user-info";

    private static final String AUTHORIZATION_HEAD = "token";

    //认证失败处理器
    private AuthenticationFailureHandler failureHandler = new AuthFailureHandler();

    /**
     * 判断认证头是否存在，不存在直接跳过
     */
    private RequestMatcher requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher(AUTHORIZATION_HEAD);

    /**
     * AuthenticationManager 接口，是认证方法的入口，接收一个Authentication对象
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
         * 处理掉不需要过滤的请求
         */
        if (!requiresAuthtication(request,response)) {
            filterChain.doFilter(request,response);
            return;
        }

        AuthenticationException failed = null;
        try {
            Authentication returnToken = null;
            boolean success = false;

            String token = request.getHeader(AUTHORIZATION_HEAD);
            if (StringUtils.isNotBlank(token)) {
                String[] parts = token.split(",");
//                //Demo1:认证演示
//                DemoToken demoToken = new DemoToken(parts[0], parts[1]);
//                returnToken = this.authenticationManager.authenticate(demoToken);
//                success = demoToken.isAuthenticated();

                //Demo2: 数据库认证演示
                UserDetails userDetails = User.builder()
                        .username(parts[0])
                        .password(parts[1])
                        .authorities(USER_INFO)
                        .build();
                //创建一个用户名+密码的凭证
                Authentication userPassToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities());
                //进入认证流程
                returnToken = this.getAuthenticationManager().authenticate(userPassToken);
                success = userPassToken.isAuthenticated();



                if (success) {
                    //认证成功，这只上下文凭证
                    SecurityContextHolder.getContext().setAuthentication(returnToken);
                    //执行后续操作
                    filterChain.doFilter(request, response);
                    return;
                }
            } else {
//                log.error("认证失败：token值为空！");
                failed = new AuthenticationServiceException("认证失败：token值为空！");
            }
        } catch (Exception e) {
            logger.error("认证有误", e);
            failed = new AuthenticationServiceException("请求头认证消息格式有误", e);
        }
        if (null == failed) {
            failed = new AuthenticationServiceException("认证失败");
        }
        //认证失败le
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request,response,failed);
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    protected boolean requiresAuthtication(HttpServletRequest request, HttpServletResponse response) {
        return requiresAuthenticationRequestMatcher.matches(request);
    }
}
