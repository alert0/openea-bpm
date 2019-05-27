package org.openbpm.sys.rest.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.sys.core.model.def.SysDataSourceDefAttribute;
import org.openbpm.sys.core.manager.SysDataSourceDefManager;
import org.openbpm.sys.core.model.SysDataSourceDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.rest.BaseController;
import org.openbpm.base.rest.util.RequestUtil;

import java.util.List;

/**
 * <pre>
 * 描述：sysDataSourceDef层的controller
 * </pre>
 */
@RestController
@RequestMapping("/sys/sysDataSourceDef/")
public class SysDataSourceDefController extends BaseController<SysDataSourceDef> {
    @Autowired
    SysDataSourceDefManager sysDataSourceDefManager;

    /**
     * <pre>
     * 根据类路径获取类字段
     * </pre>
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("initAttributes")
    @CatchErr(write2response = true, value = "初始化属性异常")
    public ResultMsg<List<SysDataSourceDefAttribute>> initAttributes(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String classPath = RequestUtil.getString(request, "classPath");
        return getSuccessResult(sysDataSourceDefManager.initAttributes(classPath));
    }

    /**
     * <pre>
     * 获取sysDataSourceDef的后端
     * 目前支持根据id 获取sysDataSourceDef
     * </pre>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getObject")
    @CatchErr(write2response = true, value = "获取sysDataSourceDef异常")
    public ResultMsg<SysDataSourceDef> getObject(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = RequestUtil.getString(request, "id");
        SysDataSourceDef sysDataSourceDef = null;
        if (StringUtil.isNotEmpty(id)) {
            sysDataSourceDef = sysDataSourceDefManager.get(id);
        }
        return getSuccessResult(sysDataSourceDef);
    }

	@Override
	protected String getModelDesc() {
		return "数据源模板";
	}
}
