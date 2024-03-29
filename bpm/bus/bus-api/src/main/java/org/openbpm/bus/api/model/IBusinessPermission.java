package org.openbpm.bus.api.model;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.bus.api.model.permission.IBusObjPermission;

public interface IBusinessPermission {

	String getObjType();

	String getObjVal();

	Map<String, ? extends IBusObjPermission> getBusObjMap();

	IBusObjPermission getBusObj(String boKey);
	
	public JSONObject getTablePermission(boolean isReadOnly);
	
	public JSONObject getPermission(boolean isReadOnly) ;
}
