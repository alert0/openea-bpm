package org.openbpm.org.rest.controller;

import javax.annotation.Resource;

import org.openbpm.org.core.manager.RoleManager;
import org.openbpm.org.core.model.Role;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.api.exception.BusinessMessage;
import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.rest.BaseController;

/**
 * 角色管理 控制器类
 */
@RestController
@RequestMapping("/org/role")
public class RoleController extends BaseController<Role> {
    @Resource
    RoleManager roleManager;


    @Override
    protected String getModelDesc() {
        return "角色";
    }

    @Override
    @CatchErr
    public ResultMsg<String> save( @RequestBody Role role) throws Exception {
        if (StringUtil.isEmpty(role.getId())) {
            boolean isExist = roleManager.isRoleExist(role);
            if (isExist) {
                throw new BusinessMessage("角色在系统中已存在!");
            }
        }
       return super.save(role);
    }

}
