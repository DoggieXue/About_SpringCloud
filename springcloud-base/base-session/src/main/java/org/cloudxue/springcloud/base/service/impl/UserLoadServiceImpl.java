package org.cloudxue.springcloud.base.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.springcloud.base.dao.SysUserDao;
import org.cloudxue.springcloud.base.dao.UserDao;
import org.cloudxue.springcloud.base.dao.po.SysUserPO;
import org.cloudxue.springcloud.base.dao.po.UserPO;
import org.cloudxue.springcloud.common.dto.UserDTO;
import org.springframework.beans.BeanUtils;

/**
 * @ClassName UserLoadServiceImpl
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/2/7 下午3:17
 * @Version 1.0
 **/
@Slf4j
public class UserLoadServiceImpl {
    private UserDao userDao;

    private SysUserDao sysUserDao;

    public UserLoadServiceImpl(UserDao userDao, SysUserDao sysUserDao) {
        this.userDao = userDao;
        this.sysUserDao = sysUserDao;
    }

    /**
     * 装载前端的用户
     *
     * @param userId
     * @return
     */
    public UserDTO loadFrontEndUser(Long userId) {
        UserPO userPO = userDao.findByUserId(userId);
        if (userPO != null) {
            UserDTO dto = new UserDTO();
            BeanUtils.copyProperties(userPO, dto);
            return dto;
        }
        return null;
    }

    /**
     * 装载后端用户
     * @param userId
     * @return
     */
    public UserDTO loadBackEndUser(Long userId) {
        SysUserPO userPO = sysUserDao.findByUserId(userId);
        if (userPO != null) {
            UserDTO dto = new UserDTO();
            BeanUtils.copyProperties(userPO, dto);
            return dto;
        }
        return null;
    }
}
