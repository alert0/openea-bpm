package org.openbpm.form.rest.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openbpm.form.generator.AbsFormElementGenerator;
import org.openbpm.form.manager.FormDefManager;
import org.openbpm.form.manager.FormTemplateManager;
import org.openbpm.form.model.FormTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.api.exception.BusinessError;
import org.openbpm.base.api.exception.BusinessMessage;
import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.api.query.QueryOP;
import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.base.core.util.PropertyUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.db.model.page.PageResult;
import org.openbpm.base.rest.BaseController;
import org.openbpm.base.rest.util.RequestUtil;
import org.openbpm.bus.api.model.IBusTableRel;
import org.openbpm.bus.api.model.IBusinessObject;
import org.openbpm.bus.api.service.IBusinessObjectService;
import org.openbpm.bus.api.service.IBusinessTableService;
import org.openbpm.form.api.model.FormType;
import org.openbpm.form.model.FormDef;
import org.openbpm.sys.api.constant.EnvironmentConstant;
import org.openbpm.sys.api.freemark.IFreemarkerEngine;
import com.github.pagehelper.Page;

/**
 * 表单管理
 * 
 */
@RestController
@RequestMapping("/form/formDef/")
public class FormDefController extends BaseController<FormDef> {
	@Autowired
	FormDefManager formDefManager;
	@Autowired
	IBusinessObjectService businessObjectService;
	@Autowired
	IBusinessTableService businessTableService;
	@Autowired
	FormTemplateManager formTemplateManager;
	@Autowired
    IFreemarkerEngine freemarkEngine;

	
	@Override
	@RequestMapping("listJson")
	public PageResult listJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String formType = RequestUtil.getString(request, "formType",FormType.PC.value());
		QueryFilter queryFilter = getQueryFilter(request);
		queryFilter.addFilter("type_", formType, QueryOP.EQUAL);
	    Page<FormDef> pageList = (Page<FormDef>) formDefManager.query(queryFilter);
	      
        return new PageResult(pageList);
	}
	
	/**
	 * formDefEdit.html的save后端
	 */
	@RequestMapping("save")
	@Override
	@CatchErr(write2response = true, value = "保存表单失败")
	public ResultMsg<String> save(@RequestBody FormDef formDef) throws Exception {
		if(StringUtil.isEmpty(formDef.getKey()) && formDefManager.getByKey(formDef.getKey())!= null) {
			throw new BusinessMessage("表单 KEY 已经存在，请修改 表单 KEY 的 值");
		}

		ResultMsg<String> msg =super.save( formDef);
		formDefManager.saveBackupHtml(formDef);
		return msg;
	}

	/**
	 * <pre>
	 * 获取formDef的后端
	 * 目前支持根据id和key 获取formDef
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getObject")
	@CatchErr(write2response = true, value = "获取formDef异常")
	public ResultMsg<JSONObject> getObject(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = RequestUtil.getString(request, "id");
		String key = RequestUtil.getString(request, "key");
		FormDef formDef = null;
		if (StringUtil.isNotEmpty(id)) {
			formDef = formDefManager.get(id);
		} else if (StringUtil.isNotEmpty(key)) {
			formDef = formDefManager.getByKey(key);
		}

		JSONObject json = formDef == null ? new JSONObject() : (JSONObject) JSON.toJSON(formDef);
		// 配置了备份路径则是开发者
		json.put("isDeveloper", StringUtil.isNotEmpty(PropertyUtil.getFormDefBackupPath()));

		return getSuccessResult(json);
	}

	/**
	 * <pre>
	 * 获取开发者备份html
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getBackupHtml")
	@CatchErr(write2response = true, value = "获取开发者备份html异常")
	public ResultMsg<String> getBackupHtml(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = RequestUtil.getString(request, "id");
		String key = RequestUtil.getString(request, "key");
		FormDef formDef = null;
		if (StringUtil.isNotEmpty(id)) {
			formDef = formDefManager.get(id);
		} else if (StringUtil.isNotEmpty(key)) {
			formDef = formDefManager.getByKey(key);
		}
		return getSuccessResult(formDefManager.getBackupHtml(formDef));
	}

	/**
	 * <pre>
	 * boTree数据树
	 * </pre>
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("boTreeData")
	@ResponseBody
	public Object boTreeData(@RequestParam String boKey) throws Exception {
		return businessObjectService.boTreeData(boKey);
	}

	/**
	 * <pre>
	 * 根据bo获取表单模板信息
	 * </pre>
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("templateData")
	@CatchErr(write2response = true, value = "根据bo获取表单模板信息异常")
	public ResultMsg<JSONArray> templateData(@RequestParam String boKey, @RequestParam String type) throws Exception {
		
		JSONArray array = formTemplateManager.templateData(boKey,type);
		
		return new ResultMsg<>(array);
	}

	/**
	 * <pre>
	 * 根据表单选择的模板生成html
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("createHtml")
	@CatchErr(write2response = true, value = "生成html异常")
	public ResultMsg<String> createHtml(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONArray jsonArray) throws Exception {
		String boKey = RequestUtil.getString(request, "boKey");
		IBusinessObject businessObject = businessObjectService.getFilledByKey(boKey);
		StringBuilder sb = new StringBuilder();
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;
			IBusTableRel relation = businessObject.getRelation().find(jsonObject.getString("tableKey"));
			FormTemplate template = formTemplateManager.getByKey(jsonObject.getString("templateKey"));
			if (template == null) {
				continue;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("relation", relation);
			
			//将所有表单生成器的实现类注入到模板引擎中
			for(AbsFormElementGenerator generator : AppUtil.getImplInstanceArray(AbsFormElementGenerator.class)) {
				map.put(generator.getGeneratorName(), generator);
			}
			
			String html = freemarkEngine.parseByString(template.getHtml(), map);
			
			sb.append(html);
		}
		if(sb.length()>0){
			sb.insert(0, "<div class=\"ivu-form ivu-form-label-right\">");
			sb.append("</div>");
		}

		return getSuccessResult(sb.toString(),"生成成功");
	}
	
    @RequestMapping("remove")
    @CatchErr
    public ResultMsg<String> remove(@RequestParam String id) throws Exception {
         String[] aryIds = StringUtil.getStringAryByStr(id);
         
         if(AppUtil.getCtxEnvironment().contains(EnvironmentConstant.SIT.key())) {
        	 throw new BusinessError("测试环境为了防止不法之徒恶意破坏演示数据，禁止表单删除！<br/>您的访问信息已经被我们统计！");
         }
         
         formDefManager.removeByIds(aryIds);
         return getSuccessResult(String.format("删除%s成功", getModelDesc()));
    }
	
	
	@Override
	protected String getModelDesc() {
		return "自定义表单";
	}

}
