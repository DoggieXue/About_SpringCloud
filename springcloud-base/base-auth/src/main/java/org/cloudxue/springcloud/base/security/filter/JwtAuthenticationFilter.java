package org.cloudxue.springcloud.base.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cloudxue.springcloud.base.security.token.JwtAuthenticationToken;
import org.cloudxue.springcloud.common.constants.SessionConstants;
import org.cloudxue.springcloud.common.context.SessionHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JwtAuthenticationFilter
 * @Description JWT凭证 处理过滤器
 * @Author xuexiao
 * @Date 2022/1/25 下午3:59
 * @Version 1.0
 **/
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private RequestMatcher requiresAuthenticationRequestMatcher;

    private List<RequestMatcher> permissiveRequestMatchers;

    /**
     * 认证管理者
     */
    private AuthenticationManager authenticationManager;

    protected AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    private AuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
    private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

    protected AuthenticationSuccessHandler getSuccessHandler() {
        return successHandler;
    }

    protected AuthenticationFailureHandler getFailureHandler() {
        return failureHandler;
    }

    public void setAuthenticationSuccessHandler(
            AuthenticationSuccessHandler successHandler) {
        Assert.notNull(successHandler, "successHandler cannot be null" );
        this.successHandler = successHandler;
    }

    public void setAuthenticationFailureHandler(
            AuthenticationFailureHandler failureHandler) {
        Assert.notNull(failureHandler, "failureHandler cannot be null" );
        this.failureHandler = failureHandler;
    }

    public JwtAuthenticationFilter () {
        this.requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher(SessionConstants.AUTHORIZATION_HEAD);
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        Assert.notNull(authenticationManager, "authenticationManager must be specified");
        Assert.notNull(successHandler, "AuthenticationSuccessHandler must be specified");
        Assert.notNull(failureHandler, "AuthenticationFailureHandler must be specified");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        /**
         * 过滤掉不需要处理的请求
         */
        if (!requiresAuthentication(request, response)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication passedToken = null;
        AuthenticationException failed = null;

        //从HTTP请求的头部获取JWT令牌的头部字段
        String token = null;
        String sessionIDStore = SessionHolder.getSessionIDStore();
        if (sessionIDStore.equals(SessionConstants.SESSION_STORE)) {//用户端存放的JWT的HTTP头部字段为token
            token = request.getHeader(SessionConstants.AUTHORIZATION_HEAD);
        } else if (sessionIDStore.equals(SessionConstants.ADMIN_SESSION_STORE)) {//管理端存放的JWT的HTTP头部字段为Authorization
            token = request.getHeader(SessionConstants.ADMIN_AUTHORIZATION_HEAD);
        } else {//未拿到头部token值，抛异常
            failed = new InsufficientAuthenticationException("请求头认证消息为空");
            unsuccessfulAuthentication(request, response, failed);
            return;
        }
        log.info("获取到请求token: " + token);
        token = StringUtils.removeStart(token, "Bearer ");

        try {
            if (StringUtils.isNotBlank(token)) {
                //组装令牌
                JwtAuthenticationToken authToken = new JwtAuthenticationToken(JWT.decode(token));
                //提交给AuthenticationManager进行令牌验证，获取认证后的令牌
                passedToken = this.getAuthenticationManager().authenticate(authToken);
                //取得认证后的用户信息，主要是用户ID
                UserDetails details = (UserDetails) passedToken.getDetails();
                //通过details.getUsername()获取用户ID，并作为请求属性进行缓存
                request.setAttribute(SessionConstants.USER_IDENTIFIER, details.getUsername());
            } else {
                failed = new InsufficientAuthenticationException("请求头认证消息为空" );
            }
        } catch (JWTDecodeException e) {
            logger.error("JWT format error", e);
            failed = new InsufficientAuthenticationException("请求头认证消息格式错误");
        } catch (InternalAuthenticationServiceException e) {
            logger.error("An internal error occurred while trying to authenticate the user.");
            failed = e;
        }

        if (passedToken != null) {
            successfulAuthentication(request, response, passedToken);
        } else if (!permissiveRequest(request)) {
            unsuccessfulAuthentication(request, response, failed);
            return;
        }

        filterChain.doFilter(request, response);
    }

    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            Authentication authResult)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    private boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return requiresAuthenticationRequestMatcher.matches(request);
    }

    protected boolean permissiveRequest(HttpServletRequest request) {
        if (permissiveRequestMatchers == null)
            return false;
        for (RequestMatcher permissiveMatcher : permissiveRequestMatchers) {
            if (permissiveMatcher.matches(request))
                return true;
        }
        return false;
    }

    public void setPermissiveUrl(String... urls) {
        if (permissiveRequestMatchers == null)
            permissiveRequestMatchers = new ArrayList<>();
        for (String url : urls)
            permissiveRequestMatchers.add(new AntPathRequestMatcher(url));
    }
}
