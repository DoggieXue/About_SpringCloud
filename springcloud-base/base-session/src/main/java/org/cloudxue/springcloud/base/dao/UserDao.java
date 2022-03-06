package org.cloudxue.springcloud.base.dao;

import org.cloudxue.springcloud.base.dao.po.UserPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/1/26 下午2:45
 * @Version 1.0
 **/
@Repository
public interface UserDao extends JpaRepository<UserPO, Long>, JpaSpecificationExecutor<UserPO> {

    UserPO findByUserId(Long id);

    Optional<UserPO> findById(Long id);

    /**
     * 根据loginName查询用户列表
     * @param loginName
     * @return
     */
    List<UserPO> findAllByUsername(String loginName);
}
