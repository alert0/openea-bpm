package org.openbpm.org.core.dao;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.org.core.model.Role;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;

/**
 * 描述：角色管理 DAO接口
 */
@MapperScan
public interface RoleDao extends BaseDao<String, Role> {
    Role getByAlias(String alias);
    /**
     * 判断角色系统中是否存在。
     *
     * @param role
     * @return
     */
    Integer isRoleExist(Role role);
    
    /**
     * 用过用户ID 获取角色
     * @param userId
     * @return
     */
	List<Role> getByUserId(String userId);
}
