package org.openbpm.security.rest.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openbpm.security.util.SubSystemUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.api.exception.BusinessMessage;
import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.base.core.util.BeanUtils;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.rest.GenericController;
import org.openbpm.base.rest.util.CookieUitl;
import org.openbpm.base.rest.util.RequestUtil;
import org.openbpm.org.api.constant.GroupTypeConstant;
import org.openbpm.org.api.model.IGroup;
import org.openbpm.org.api.model.IUser;
import org.openbpm.org.api.model.system.ISubsystem;
import org.openbpm.org.api.model.system.ISysResource;
import org.openbpm.org.api.service.GroupService;
import org.openbpm.org.api.service.SysResourceService;
import org.openbpm.sys.api.constant.ResouceTypeConstant;
import org.openbpm.sys.util.ContextUtil;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 用户资源
 *
 */
@RestController
@Api(description = "用户资源信息")
public class UserResourceController extends GenericController {
    @Resource
    GroupService orgService;
    @Resource
    SysResourceService sysResourceService;


    @RequestMapping(value = "/org/userResource/userMsg", method = {RequestMethod.POST, RequestMethod.GET})
    @CatchErr
    @ApiOperation(value = "用户信息", notes = "获取用户信息，当前组织，可切换的组织岗位，当前系统，拥有的系统列表等信息")
    public ResultMsg userMsg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String systemAlias = RequestUtil.getString(request, "system");
        if (StringUtil.isEmpty(systemAlias)) {
            systemAlias = SubSystemUtil.getSystemId(request);
        }

        List<ISubsystem> subsystemList = sysResourceService.getCuurentUserSystem();
        if (CollectionUtil.isEmpty(subsystemList)) {
            throw new BusinessMessage("当前用户尚未分配任何资源权限！");
        }

        ISubsystem currentSystem = null;
        //获取当前系统
        if (StringUtil.isNotEmpty(systemAlias)) {
            for (ISubsystem system : subsystemList) {
                if (system.getAlias().equals(systemAlias)) {
                    currentSystem = system;
                    break;
                }
            }
        } else {
            //获取默认系统
            currentSystem = sysResourceService.getDefaultSystem(ContextUtil.getCurrentUserId());
        }

        //没有之前使用的系统
        if (currentSystem == null) {
            currentSystem = subsystemList.get(0);
        }
        SubSystemUtil.setSystemId(request, response, currentSystem.getAlias());

        IGroup group = ContextUtil.getCurrentGroup();
        List<? extends IGroup> orgList = orgService.getGroupsByGroupTypeUserId(GroupTypeConstant.ORG.key(), ContextUtil.getCurrentUserId());

        ResultMsg result = getSuccessResult()
                .addMapParam("currentEnviroment", AppUtil.getCtxEnvironment())
                .addMapParam("subsystemList", subsystemList)
                .addMapParam("currentSystem", currentSystem)
                .addMapParam("currentOrg", group)
                .addMapParam("orgList", orgList)
                .addMapParam("user", ContextUtil.getCurrentUser());

        getSysResource(result, currentSystem.getId());

        return result;
    }

    // 切换系统
    @RequestMapping("userResource/changeSystem")
    public ResultMsg changeSystem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String systemAlias = RequestUtil.getString(request, "systemAlias");
        SubSystemUtil.setSystemId(request, response, systemAlias);

        return getSuccessResult("切换成功");
    }

    // 切换组织
    @RequestMapping("userResource/changeOrg")
    public ResultMsg changeOrg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = RequestUtil.getString(request, "orgId");
        IGroup org = orgService.getById(GroupTypeConstant.ORG.key(), id);
        ContextUtil.setCurrentOrg(org);
        CookieUitl.addCookie("currentOrg", id, CookieUitl.cookie_no_expire);

        return getSuccessResult("切换成功");
    }

    private void getSysResource(ResultMsg result, String systemId) {
        IUser user = ContextUtil.getCurrentUser();
        boolean isAdmin = ContextUtil.isAdmin(user);
        List<ISysResource> list = null;
        if (isAdmin) {
            list = sysResourceService.getBySystemId(systemId);
        } else {
            list = sysResourceService.getBySystemAndUser(systemId, user.getUserId());
        }

        // 菜单和按钮分离
        List<ISysResource> menuList = new ArrayList<>();
        Map<String, Boolean> buttonPermision = new HashMap<>();

        list.forEach(resouces -> {
            if (ResouceTypeConstant.MENU.getKey().equals(resouces.getType())) {
                menuList.add(resouces);
            } else {
                buttonPermision.put(resouces.getAlias(), resouces.getEnable() == 1);
            }
        });


        result.addMapParam("userMenuList", BeanUtils.listToTree(menuList));
        result.addMapParam("buttonPermision", buttonPermision);
    }
}
