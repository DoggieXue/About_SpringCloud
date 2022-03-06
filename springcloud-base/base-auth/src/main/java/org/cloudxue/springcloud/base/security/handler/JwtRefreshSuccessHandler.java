package org.cloudxue.springcloud.base.security.handler;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.cloudxue.springcloud.base.security.token.JwtAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @ClassName JwtRefreshSuccessHandler
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/26 上午11:16
 * @Version 1.0
 **/
public class JwtRefreshSuccessHandler implements AuthenticationSuccessHandler {
    /**
     * 刷新间隔时间 5分钟
     */
    private static final int tokenRefreshInterval = 300;

    public JwtRefreshSuccessHandler() {

    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DecodedJWT auth = ((JwtAuthenticationToken) authentication).getDecodedJWT();
        boolean shouldRefresh = shouldTokenRefresh(auth.getIssuedAt());
        if (shouldRefresh) {
            //TODO 生成新的token返回
        }
    }

    protected boolean shouldTokenRefresh(Date issueAt) {
        LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(tokenRefreshInterval).isAfter(issueTime);
    }
}
