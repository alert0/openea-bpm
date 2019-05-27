package org.openbpm.sys.rest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openbpm.sys.core.manager.ScriptManager;
import org.openbpm.sys.core.model.Script;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.core.id.IdGenerator;
import org.openbpm.base.rest.BaseController;
import org.openbpm.base.rest.util.RequestUtil;
import org.openbpm.sys.api.groovy.IGroovyScriptEngine;

@RestController
@RequestMapping("/sys/script/")
public class SysScriptController extends BaseController<Script> {

    @Resource
    private ScriptManager scriptManager;
    @Resource
    private IdGenerator idGenerator;
    @Resource
    IGroovyScriptEngine groovyScriptEngine;

   

    /**
     * 编辑@名称：系统脚本信息页面
     *
     * @param request
     * @param response
     * @return
     * @throws Exception ModelAndView
     */
    @RequestMapping("getCategoryList")
    public List<String> getCategoryList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> categoryList = scriptManager.getDistinctCategory();
        return categoryList;
    }


    @RequestMapping("executeScript")
    public Object executeScript(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String script = RequestUtil.getString(request, "script");
        String key = RequestUtil.getString(request, "key");
        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject jsonObject = new JSONObject();
        try {
            Object obj = groovyScriptEngine.executeObject(script, map);
            jsonObject.put("val", obj);
            jsonObject.put("success", true);
        } catch (Exception e) {
        	e.printStackTrace();
            jsonObject.put("val", "出错了" + e.getMessage());
        }
        jsonObject.put("key", key);
        jsonObject.put("script", script);
        return jsonObject;
    }

	@Override
	protected String getModelDesc() {
		return "常用脚本";
	}

}
