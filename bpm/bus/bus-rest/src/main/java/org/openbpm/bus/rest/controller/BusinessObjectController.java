package org.openbpm.bus.rest.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import org.openbpm.bus.manager.BusinessObjectManager;
import org.openbpm.bus.model.BusinessObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.rest.BaseController;
import org.openbpm.base.rest.util.RequestUtil;
import org.openbpm.bus.util.BusinessObjectCacheUtil;

/**
 * <pre>
 * 描述：businessObject层的controller
 * </pre>
 */
@RestController
@RequestMapping("/bus/businessObject/")
public class BusinessObjectController extends BaseController<BusinessObject> {
	@Resource
    BusinessObjectManager businessObjectManager;

	/**
	 * <pre>
	 * 获取businessObject的后端
	 * 目前支持根据id,key 获取businessObject
	 * </pre>
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getObject")
	@CatchErr(write2response = true, value = "获取businessObject异常")
	public ResultMsg<BusinessObject> getObject(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = RequestUtil.getString(request, "id");
		String key = RequestUtil.getString(request, "key");
		boolean fill = RequestUtil.getBoolean(request, "fill");// 是否要填充table
		BusinessObject object = null;
		if (StringUtil.isNotEmpty(id)) {
			object = businessObjectManager.get(id);
		} else if (StringUtil.isNotEmpty(key)) {
			object = businessObjectManager.getByKey(key);
		}
		if (fill && object != null) {
			object = businessObjectManager.getFilledByKey(object.getKey());
		}

		return getSuccessResult(object);
	}
	
	@Override
	 public ResultMsg<String> save(@RequestBody BusinessObject businessObject) throws Exception {
		 ResultMsg<String> resultMsg= super.save(businessObject);
		 BusinessObjectCacheUtil.putDataSourcesKeys(businessObject.getKey(), businessObjectManager.getFilledByKey(businessObject.getKey()).calDataSourceKeys());
		 return resultMsg;
	 }

	@RequestMapping("getOverallArrangement")
	public ResultMsg<JSON> getOverallArrangement(String boCode){
		//TODO see example_getOverallArrangement.json
		// tabList/groupList/columnList
		// groupList/columnList
		return null;
	}
	
	@Override
	protected String getModelDesc() {
		return "业务对象";
	}

}
