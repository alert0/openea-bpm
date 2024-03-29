package org.openbpm.form.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openbpm.form.manager.FormDefManager;
import org.openbpm.form.service.FormDefDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.dao.CommonDao;
import org.openbpm.base.db.datasource.DbContextHolder;
import org.openbpm.base.db.model.page.PageResult;
import org.openbpm.base.rest.GenericController;
import org.openbpm.base.rest.util.RequestUtil;
import org.openbpm.bus.api.constant.BusinessPermissionObjType;
import org.openbpm.bus.api.model.IBusinessObject;
import org.openbpm.bus.api.model.IBusinessPermission;
import org.openbpm.bus.api.model.IBusinessTable;
import org.openbpm.bus.api.service.IBusinessDataService;
import org.openbpm.bus.api.service.IBusinessObjectService;
import org.openbpm.bus.api.service.IBusinessPermissionService;
import org.openbpm.form.api.constant.FormStatusCode;
import org.openbpm.form.model.FormDef;
import org.openbpm.form.model.FormDefData;
import org.openbpm.sys.api.model.ISysDataSource;
import org.openbpm.sys.api.service.ISysDataSourceService;

/**
 * <pre>
 * 描述：表单数据的controller
 * </pre>
 */
@RestController
@RequestMapping("/form/formDefData/")
public class FormDefDataController extends GenericController {
	@Autowired
	FormDefDataService formDefDataService;
	@Autowired
	IBusinessDataService businessDataService;
	@Autowired
	IBusinessObjectService businessObjectService;
	@Autowired
	ISysDataSourceService sysDataSourceService;
	@Autowired
	CommonDao<?> commonDao;
	@Autowired
	IBusinessPermissionService businessPermissionService;
	@Autowired
	FormDefManager formDefManager;

	/**
	 * <pre>
	 * 获取FormDefData的后端
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getData")
	@CatchErr(write2response = true, value = "获取FormDefData异常")
	public ResultMsg<FormDefData> getFormDefData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String key = RequestUtil.getString(request, "key");
		String id = RequestUtil.getString(request, "id", null);
		FormDefData formDefData = formDefDataService.getByFormDefKey(key, id);
		return getSuccessResult(formDefData);
	}

	/**
	 * <pre>
	 * 保存formDef中的data数据
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * @param data
	 * @throws Exception
	 */
	@RequestMapping("saveData")
	@CatchErr(write2response = true, value = "保存formDef中的data数据异常")
	public ResultMsg<String> saveData(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject data) throws Exception {
		String key = RequestUtil.getString(request, "key");
		FormDef formDef = formDefManager.getByKey(key);
		IBusinessPermission permission = businessPermissionService.getByObjTypeAndObjVal(BusinessPermissionObjType.FORM.getKey(), key, formDef.getBoKey(), true);
		businessDataService.saveFormDefData(data, permission);
		return getSuccessResult("保存数据成功");
	}

	/**
	 * <pre>
	 * 获取bo的数据列表
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * @param key
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getList_{boKey}")
	@CatchErr(write2response = true, value = "获取对话框的列表数据失败")
	public PageResult getList(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "boKey") String boKey) throws Exception {
		QueryFilter queryFilter = getQueryFilter(request);
		// 页面来的参数
		IBusinessObject businessObject = businessObjectService.getFilledByKey(boKey);
		IBusinessTable businessTable = businessObject.getRelation().getTable();
		ISysDataSource sysDataSource = sysDataSourceService.getByKey(businessTable.getDsKey());
		// 切换数据源
		DbContextHolder.setDataSource(sysDataSource.getKey(), sysDataSource.getDbType());
		String sql = "select * from " + businessTable.getName();
		List<?> list = commonDao.queryForListPage(sql, queryFilter);
		return new PageResult(list);
	}

	@RequestMapping("removeData/{formKey}/{id}")
	@CatchErr(write2response = true, value = "删除formDef中的data数据异常")
	public ResultMsg removeData(@PathVariable(value = "formKey", required = false) String formKey, @PathVariable(value = "id", required = false) String id) throws Exception {
		if (StringUtil.isEmpty(formKey)) {
			throw new BusinessException("formKey 不能为空", FormStatusCode.PARAM_ILLEGAL);
		}
		if (StringUtil.isEmpty(id)) {
			throw new BusinessException("ID 不能为空", FormStatusCode.PARAM_ILLEGAL);
		}

		FormDef formDef = formDefManager.getByKey(formKey);
		String boKey = formDef.getBoKey();

		IBusinessPermission permission = businessPermissionService.getByObjTypeAndObjVal(BusinessPermissionObjType.FORM.getKey(), formKey, formDef.getBoKey(), true);
		IBusinessObject businessObject = businessObjectService.getFilledByKey(boKey);
		businessObject.setPermission(permission.getBusObj(boKey));

		businessDataService.removeData(businessObject, id);

		return getSuccessResult("删除成功！");
	}

}
