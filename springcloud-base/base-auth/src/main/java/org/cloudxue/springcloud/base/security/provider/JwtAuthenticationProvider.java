package org.cloudxue.springcloud.base.security.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;
import org.cloudxue.springcloud.base.security.token.JwtAuthenticationToken;
import org.cloudxue.springcloud.common.constants.SessionConstants;
import org.cloudxue.springcloud.common.dto.UserDTO;
import org.cloudxue.springcloud.common.util.JsonUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;

import java.util.Calendar;

import static org.cloudxue.springcloud.common.context.SessionHolder.G_USER;

/**
 * @ClassName JwtAuthenticationProvider
 * @Description JWT凭证 认证服务提供者
 * @Author xuexiao
 * @Date 2022/1/25 下午3:58
 * @Version 1.0
 **/
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private RedisOperationsSessionRepository sessionRepository;

    public JwtAuthenticationProvider(RedisOperationsSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //判断JWT令牌是否过期
        DecodedJWT jwt = ((JwtAuthenticationToken)authentication).getDecodedJWT();
        if (jwt.getExpiresAt().before(Calendar.getInstance().getTime())) {
            throw new NonceExpiredException("认证过期");
        }
        //取的 session id
        String sid = jwt.getSubject();
        //获取令牌字符串，用于验证是否重复登录
        String newToken = jwt.getToken();
        //获取session
        Session session = null;
        try {
//            session = sessionRepository.findById(sid);
            session = sessionRepository.getSession(sid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == session) {
            throw new NonceExpiredException("还没登录，请登录系统");
        }

        String json = session.getAttribute(G_USER);
        if (StringUtils.isBlank(json)) {
            throw new NonceExpiredException("认证有误，请重新登录");
        }

        //获取session中的用户信息
        UserDTO userDTO = JsonUtil.jsonToPojo(json, UserDTO.class);
        if (null == userDTO) {
            throw new NonceExpiredException("认证有误，请重新登录");
        }
        //判断是否在其他地方登录
        if (null == newToken || !newToken.equals(userDTO.getToken())) {
            throw new NonceExpiredException("您已经在其他地方登录！");
        }

        String userID = null;
        if (null == userDTO.getUserId()) {
            userID = String.valueOf(userDTO.getId());
        } else {
            userID = String.valueOf(userDTO.getUserId());
        }
        UserDetails userDetails = User.builder()
                .username(userID)
                .password(userDTO.getPassword())
                .authorities(SessionConstants.USER_INFO)
                .build();

        try {
            //用户密码的密文，作为JWT 的加密盐
            String encryptSalt = userDTO.getPassword();
            Algorithm algorithm = Algorithm.HMAC256(encryptSalt);
            //创建验证器
            JWTVerifier verifier = JWT.require(algorithm).withSubject(sid).build();
            //进行jwt token验证
            verifier.verify(newToken);
        } catch (Exception e) {
            throw new BadCredentialsException("认证有误：令牌校验失败，请重新登录", e);
        }
        //返回认证通过的token，包含用户的id等信息
        JwtAuthenticationToken passedToken = new JwtAuthenticationToken(
                userDetails,
                jwt,
                userDetails.getAuthorities());
        return passedToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }
}
