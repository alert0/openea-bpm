package org.openbpm.org.core.manager.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.openbpm.org.core.manager.RoleManager;
import org.openbpm.org.core.model.Role;
import org.springframework.stereotype.Service;

import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.org.core.dao.RoleDao;

/**
 * <pre>
 * 描述：角色管理 处理实现类
 * </pre>
 */
@Service("roleManager")
public class RoleManagerImpl extends BaseManager<String, Role> implements RoleManager {
    @Resource
    RoleDao roleDao;

    public Role getByAlias(String alias) {
        return roleDao.getByAlias(alias);
    }
    
    @Override
    public List<Role> getByUserId(String userId) {
    	if(StringUtil.isEmpty(userId))return Collections.emptyList();
        return roleDao.getByUserId(userId);
    }

    @Override
    public boolean isRoleExist(Role role) {
        return roleDao.isRoleExist(role) != 0;
    }

}
