package org.cloudxue.springcloud.base.security.token;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.cloudxue.springcloud.common.constants.SessionConstants;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * @ClassName JwtAuthenticationToken
 * @Description JWT凭证/令牌
 * @Author xuexiao
 * @Date 2022/1/25 下午3:53
 * @Version 1.0
 **/
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * 用户信息：用户ID、密码
     */
    private UserDetails userDetails;

    /**
     *  封装的 JWT 认证信息
     */
    private DecodedJWT decodedJWT;

    public JwtAuthenticationToken(DecodedJWT decodedJWT) {
        super(Collections.emptyList());
        this.decodedJWT = decodedJWT;
    }

    public JwtAuthenticationToken(UserDetails userDetails,
                                  DecodedJWT decodedJWT,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userDetails = userDetails;
        this.decodedJWT = decodedJWT;
    }

    @Override
    public void setDetails(Object details) {
        super.setDetails(details);
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return userDetails;
    }

    @Override
    public Object getPrincipal() {
        return decodedJWT.getSubject();
    }

    public DecodedJWT getDecodedJWT() {
        return decodedJWT;
    }

    public OAuth2AccessToken createAccessToken () {
        DefaultOAuth2AccessToken auth2AccessToken = new DefaultOAuth2AccessToken(decodedJWT.getToken());

        long validitySeconds = decodedJWT.getExpiresAt().getDate() - System.currentTimeMillis();
        auth2AccessToken.setExpiration(new Date(System.currentTimeMillis() + validitySeconds));

        auth2AccessToken.setRefreshToken(null);
        auth2AccessToken.setScope(Collections.singleton(SessionConstants.DEFAULT_SCOPE));
        return auth2AccessToken;
    }
}
