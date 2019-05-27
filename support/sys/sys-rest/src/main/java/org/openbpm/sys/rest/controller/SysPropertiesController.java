package org.openbpm.sys.rest.controller;


import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.sys.core.manager.SysPropertiesManager;
import org.openbpm.sys.core.model.SysProperties;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.api.exception.BusinessMessage;
import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.core.id.IdUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.db.model.page.PageResult;
import org.openbpm.base.rest.GenericController;
import org.openbpm.base.rest.util.RequestUtil;
import com.github.pagehelper.Page;
import org.springframework.web.bind.annotation.RestController;


/**
 * 系统属性
 */
@RestController
@RequestMapping("/sys/sysProperties/")
public class SysPropertiesController extends GenericController {
    @Resource
    SysPropertiesManager sysPropertiesManager;

    /**
     * 系统属性列表(分页条件查询)数据
     */
    @RequestMapping("listJson")
    public @ResponseBody
    PageResult listJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
        QueryFilter queryFilter = getQueryFilter(request);
        Page<SysProperties> sysPropertiesList = (Page<SysProperties>) sysPropertiesManager.query(queryFilter);
        return new PageResult(sysPropertiesList);
    }


    /**
     * 系统属性明细页面
     */
    @RequestMapping("getJson")
    @CatchErr(write2response = true)
    public @ResponseBody ResultMsg<SysProperties> getJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = RequestUtil.getString(request, "id");
        SysProperties sysProperties = new SysProperties();

        List<String> groups = sysPropertiesManager.getGroups();
        if (StringUtil.isEmpty(id)) {
            sysProperties.setCategorys(groups);
        }else{
            sysProperties = sysPropertiesManager.get(id);
            sysProperties.setCategorys(groups);
        }

        return getSuccessResult(sysProperties);
    }

    /**
     * 保存系统属性信息
     *
     * @param request
     * @param response
     * @param sysProperties
     * @throws Exception void
     * @throws
     */
    @RequestMapping("save")
    @CatchErr("对系统属性操作失败")
    public ResultMsg<String> save(HttpServletRequest request, HttpServletResponse response, @RequestBody SysProperties sysProperties) throws Exception {
        String resultMsg = null;

        boolean isExist = sysPropertiesManager.isExist(sysProperties);
        if (isExist) {
            throw new BusinessMessage("别名系统中已存在!");
        }

        String id = sysProperties.getId();
        sysProperties.setValByEncrypt();

        if (StringUtil.isEmpty(id)) {
            sysProperties.setId(IdUtil.getSuid());
            sysProperties.setCreateTime(new Date());
            sysPropertiesManager.create(sysProperties);
            resultMsg = "添加系统属性成功";
        } else {
            sysPropertiesManager.update(sysProperties);
            resultMsg = "更新系统属性成功";
        }

        sysPropertiesManager.reloadProperty();

        return getSuccessResult(resultMsg);
    }

    /**
     * 批量删除系统属性记录
     */
    @RequestMapping("remove")
    @CatchErr("删除系统属性失败")
    public ResultMsg<String> remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] aryIds = RequestUtil.getStringAryByStr(request, "id");

        sysPropertiesManager.removeByIds(aryIds);
        return getSuccessResult("删除系统属性成功");
    }
}
