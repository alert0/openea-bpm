package org.openbpm.org.core.manager;

import org.openbpm.base.manager.Manager;
import org.openbpm.org.core.model.Role;

import java.util.List;

/**
 * <pre>
 * 描述：角色管理 处理接口
 * </pre>
 */
public interface RoleManager extends Manager<String, Role> {


    Role getByAlias(String alias);

    /**
     * 判断角色是否存在。
     *
     * @param role
     * @return
     */
    boolean isRoleExist(Role role);
    
    /**
     * 根据用户ID获取角色列表
     *
     * @param userId
     * @return
     */
	List<Role> getByUserId(String userId);

}
