package org.openbpm.sys.permission.impl;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.sys.api.permission.IPermissionCalculator;
/**
 * <pre>
 * 描述：无人
 * </pre>
 */
@Service
public class NonePermissionCalculator implements IPermissionCalculator {

	@Override
	public String getTitle() {
		return "无";
	}

	@Override
	public String getType() {
		return "none";
	}

	@Override
	public boolean haveRights(JSONObject json) {
		return false;
	}

}
