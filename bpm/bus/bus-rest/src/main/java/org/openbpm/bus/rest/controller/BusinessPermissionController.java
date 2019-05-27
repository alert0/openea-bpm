package org.openbpm.bus.rest.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.bus.manager.BusinessObjectManager;
import org.openbpm.bus.manager.BusinessPermissionManager;
import org.openbpm.bus.model.BusinessObject;
import org.openbpm.bus.model.BusinessPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.rest.BaseController;
import org.openbpm.base.rest.util.RequestUtil;

/**
 * <pre>
 * 描述：businessPermission层的controller
 * </pre>
 */
@RestController
@RequestMapping("/bus/businessPermission/")
public class BusinessPermissionController extends BaseController<BusinessPermission> {
	@Resource
    BusinessObjectManager businessObjectManager;
	@Autowired
    BusinessPermissionManager businessPermissionManager;

	/**
	 * <pre>
	 * 获取businessPermission的后端
	 * </pre>
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getObject")
	@CatchErr(write2response = true, value = "获取businessPermission异常")
	public ResultMsg<BusinessPermission> getObject(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String objType = RequestUtil.getString(request, "objType");
		String objVal = RequestUtil.getString(request, "objVal");
		BusinessPermission businessPermission = businessPermissionManager.getByObjTypeAndObjVal(objType, objVal);
		return getSuccessResult(businessPermission);
	}

	/**
	 * <pre>
	 * 获取bo数据的后端
	 * </pre>
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getBo")
	@CatchErr(write2response = true, value = "获取boo异常")
	public ResultMsg<Map<String, BusinessObject>> getBo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] boKeys = RequestUtil.getStringAryByStr(request, "boKeys");
		
		Map<String, BusinessObject> boMap = new HashMap<>();
		for (String boKey : boKeys) {
			BusinessObject bo = businessObjectManager.getFilledByKey(boKey);
			boMap.put(boKey, bo);
		}
		return getSuccessResult(boMap);
	}

	@Override
	protected String getModelDesc() {
		return "业务对象权限";
	}

}
