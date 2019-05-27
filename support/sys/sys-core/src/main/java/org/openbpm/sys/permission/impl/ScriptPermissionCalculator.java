package org.openbpm.sys.permission.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.sys.api.groovy.IGroovyScriptEngine;
import org.openbpm.sys.api.permission.IPermissionCalculator;

/**
 * <pre>
 * 描述：脚本
 * </pre>
 */
@Service
public class ScriptPermissionCalculator implements IPermissionCalculator {
	@Resource
	IGroovyScriptEngine groovyScriptEngine;

	@Override
	public String getTitle() {
		return "脚本";
	}

	@Override
	public String getType() {
		return "script";
	}

	@Override
	public boolean haveRights(JSONObject json) {
		String script = json.getString("id");
		return groovyScriptEngine.executeBoolean(script, null);
	}

}
