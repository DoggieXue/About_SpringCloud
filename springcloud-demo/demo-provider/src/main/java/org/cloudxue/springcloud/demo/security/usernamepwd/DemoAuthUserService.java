package org.cloudxue.springcloud.demo.security.usernamepwd;

import org.cloudxue.springcloud.common.constants.SessionConstants;
import org.cloudxue.springcloud.standard.context.SpringContextUtil;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName DemoAuthUserService
 * @Description 定制的UserDetailsService接口实现类
 * @Author xuexiao
 * @Date 2022/1/25 上午11:34
 * @Version 1.0
 **/
@Service
public class DemoAuthUserService implements UserDetailsService {

    /**
     * 模拟的数据源，实际从DB中获取
     */
    private Map<String, String> map = new LinkedHashMap<>();

    //初始化模拟的数据源，放入两个用户
    {
        map.put("zhangsan","123456");
        map.put("lisi","123456");
    }

    /**
     * 装载系统配置的加密器
     */
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //实际场景中，需要从DB中获取
        String passwordInDB = map.get(username);
        if (null == passwordInDB) {
            return null;
        }

        if (null == passwordEncoder) {
            passwordEncoder = SpringContextUtil.getBean(PasswordEncoder.class);
        }

        /**
         * 返回一个用户详细实例：包含用户名、密码、权限清单、用户角色
         */
        UserDetails userDetails = User.builder()
                .username(username)
                .password(passwordEncoder.encode(passwordInDB))
                .authorities(SessionConstants.USER_INFO)
                .roles("USER")
                .build();

        return userDetails;
    }
}
