package org.cloudxue.springcloud.demo.security.usernamepwd;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.cloudxue.springcloud.common.constants.SessionConstants;
import org.cloudxue.springcloud.demo.security.AuthFailureHandler;
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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName UsernamePasswordAuthFilter
 * @Description:
 * @Author: Doggie
 * @Date: 2023年02月17日 16:02:26
 * @Version 1.0
 **/
@Slf4j
public class UsernamePasswordAuthFilter extends OncePerRequestFilter {

    private AuthenticationManager authenticationManager;

    /**
     * 认证失败处理器
     */
    private AuthenticationFailureHandler failureHandler = new AuthFailureHandler();

    private RequestMatcher authenticationRequestHeaderMatcher = new RequestHeaderRequestMatcher(SessionConstants.AUTHORIZATION_HEAD);

    private boolean isHaveToken(HttpServletRequest request){
        return authenticationRequestHeaderMatcher.matches(request);
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        Assert.notNull(failureHandler, "AuthenticationFailureHandler must be specified!");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //校验请求头是否含有token
//        if (!requireAuthentication(request)) {
//            log.info("无需校验...");
//            filterChain.doFilter(request,response);
//            return;
//        }
        AuthenticationException failed = null;
        if (!isHaveToken(request)) {
            log.info("请求头不含令牌字段...");
            failed = new AuthenticationServiceException("请求头格式有误，缺少令牌token字段");
        } else {
            log.info("开始令牌校验....");
            try {
                Authentication returnToken = null;
                boolean isSuccess = false;

                //获取用户信息，组装成token，交给 AuthenticationManager进行校验
                String token = request.getHeader(SessionConstants.AUTHORIZATION_HEAD);
                if (StringUtils.isEmpty(token)) {
                    failed = new AuthenticationServiceException("认证失败！token值为空");
                } else {
                    String[] userInfos = token.split(",");
                    log.info("根据令牌，构建UserDetails对象...");
                    UserDetails userDetails = User.builder().username(userInfos[0])
                            .password(userInfos[1])
                            .authorities(SessionConstants.USER_INFO)
                            .build();
                    log.info("构建UsernamePasswordAuthenticationToken对象...");
                    Authentication usernamePasswordAuthToken = new UsernamePasswordAuthenticationToken(userDetails,
                            userDetails.getPassword(),userDetails.getAuthorities());
                    log.info("将UsernamePasswordAuthenticationToken对象提交给AuthenticationManager进行认证");
                    returnToken = this.authenticationManager.authenticate(usernamePasswordAuthToken);
                    log.info("认证返回的token: {}", returnToken);

                    isSuccess = usernamePasswordAuthToken.isAuthenticated();
                    if (isSuccess) {
                        //认证成功，设置上下文令牌
                        SecurityContextHolder.getContext().setAuthentication(returnToken);
                        //执行后续操作
                        filterChain.doFilter(request,response);
                        return;
                    }
                }
            } catch (Exception e) {
                log.error("认证失败",e);
                failed = new AuthenticationServiceException("请求头认证消息格式有误", e);
            }
        }


        if (null == failed) {
            failed = new AuthenticationServiceException("认证失败！");
        }
        //认证失败后的处理
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request,response,failed);
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
