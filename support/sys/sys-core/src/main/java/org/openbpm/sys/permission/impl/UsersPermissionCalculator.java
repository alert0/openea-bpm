package org.openbpm.sys.permission.impl;

import org.openbpm.sys.api.permission.IPermissionCalculator;
import org.openbpm.sys.util.ContextUtil;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

/**
 * <pre>
 * 描述：用户
 * </pre>
 */
@Service
public class UsersPermissionCalculator implements IPermissionCalculator {

	@Override
	public String getTitle() {
		return "用户";
	}

	@Override
	public String getType() {
		return "user";
	}

	@Override
	public boolean haveRights(JSONObject json) {
		for(String id :json.getString("id").split(",")) {
			if(id.equals(ContextUtil.getCurrentUserId())) {
				return true;
			}
		}
		return false;
	}

}
