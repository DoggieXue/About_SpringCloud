package org.cloudxue.springcloud.base.dao;

import org.cloudxue.springcloud.base.dao.po.SysUserPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName SysUserDao
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/2/7 下午3:19
 * @Version 1.0
 **/
public interface SysUserDao extends JpaRepository<SysUserDao, Long>, JpaSpecificationExecutor<SysUserDao> {

    SysUserPO findByUserId(Long id);

    Optional<SysUserDao> findById(Long id);

    List<SysUserPO> findAllByUsername(String loginName);
}
