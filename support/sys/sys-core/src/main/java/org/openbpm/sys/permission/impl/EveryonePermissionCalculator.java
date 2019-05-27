package org.openbpm.sys.permission.impl;


import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.sys.api.permission.IPermissionCalculator;

/**
 * <pre>
 * 描述：所有人
 * </pre>
 */
@Service
public class EveryonePermissionCalculator implements IPermissionCalculator {

	@Override
	public String getType() {
		return "everyone";
	}

	@Override
	public String getTitle() {
		return "所有人";
	}

	@Override
	public boolean haveRights(JSONObject json) {
		return true;
	}

}
