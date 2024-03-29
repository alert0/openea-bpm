package org.openbpm.bus.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.openbpm.base.core.model.BaseModel;
import org.openbpm.base.core.util.JsonUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bus.api.constant.RightsType;
import org.openbpm.bus.api.model.IBusinessPermission;
import org.openbpm.bus.api.model.permission.IBusColumnPermission;
import org.openbpm.bus.api.model.permission.IBusObjPermission;
import org.openbpm.bus.api.model.permission.IBusTablePermission;
import org.openbpm.bus.model.permission.BusObjPermission;

/**
 * <pre>
 * 描述：业务权限对象
 * </pre>
 */
@Data
public class BusinessPermission extends BaseModel implements IBusinessPermission {
	/**
	 * <pre>
	 * 配置这个权限的对象，可以是表单，流程，或流程节点
	 * BusinessPermissionObjType
	 * </pre>
	 */
	private String objType;
	/**
	 * <pre>
	 * 能获取到配置权限的对象的唯一值
	 * 通常是key 或 id 
	 * 可以是自定义的
	 * 例如 某个流程的某个节点，可以是 流程key.nodeKey
	 * 这样的格式
	 * </pre>
	 */
	private String objVal;
	/**
	 * <pre>
	 * busObjMap的json数据
	 * </pre>
	 */
	private String busObjMapJson;
	// 以下字段跟数据库无关
	/**
	 * <pre>
	 * 权限map
	 * Map<boKey,BusObjPermission>
	 * </pre>
	 */
	private Map<String, BusObjPermission> busObjMap = new HashMap<>();


	public void setBusObjMapJson(String busObjMapJson) {
		this.busObjMapJson = busObjMapJson;
		if (StringUtil.isEmpty(busObjMapJson)) {
			this.busObjMap = null;
			return;
		}
		
		busObjMap = new HashMap<>();
		Map<String, Object> map = JSONObject.parseObject(busObjMapJson, Map.class);
		for (Entry<String, Object> entry : map.entrySet()) {
			busObjMap.put(entry.getKey(), JSONObject.parseObject(entry.getValue().toString(), BusObjPermission.class));
		}
	}


	public void setBusObjMap(Map<String, BusObjPermission> busObjMap) {
		this.busObjMap = busObjMap;
		this.busObjMapJson = JsonUtil.toJSONString(busObjMap);
	}
	
	@Override
	public BusObjPermission getBusObj(String boKey) {
		return busObjMap.get(boKey);
	}
	
	public JSONObject getTablePermission(boolean isReadonly) {
		handlerPermission(isReadonly);
		return tablePermission;
	}
	
	public JSONObject getPermission(boolean isReadonly) {
		handlerPermission(isReadonly);
		return permission;
	}
	
	/**
	 *   readOnly 类型的降级处理 比如在某个节点的实例表单中。 权限需要将w b 改成r 用来展示历史表单
	 */
	private JSONObject tablePermission;
	private JSONObject permission = null;
	private synchronized void handlerPermission(Boolean isReadonly) {
		if(permission != null) return;
		
		tablePermission = new JSONObject();
		permission = new JSONObject();
		
		for (Entry<String, ? extends IBusObjPermission> entry : this.getBusObjMap().entrySet()) {
			IBusObjPermission busObjPermission = entry.getValue();
			permission.put(busObjPermission.getKey(), new JSONObject());
			tablePermission.put(busObjPermission.getKey(), new JSONObject());
			for (Entry<String, ? extends IBusTablePermission> etry : busObjPermission.getTableMap().entrySet()) {
				IBusTablePermission busTablePermission = etry.getValue();
				permission.getJSONObject(busObjPermission.getKey()).put(busTablePermission.getKey(), new JSONObject());
				tablePermission.getJSONObject(busObjPermission.getKey()).put(busTablePermission.getKey(), handelReadonlyResult(busTablePermission.getResult(),isReadonly));

				for (Entry<String, ? extends IBusColumnPermission> ery : busTablePermission.getColumnMap().entrySet()) {
					IBusColumnPermission busColumnPermission = ery.getValue();
					permission.getJSONObject(busObjPermission.getKey()).getJSONObject(busTablePermission.getKey()).put(busColumnPermission.getKey(), handelReadonlyResult(busColumnPermission.getResult(),isReadonly));
				}
			}
		}
	}
	
	private String handelReadonlyResult(String result,Boolean isReadonly){
		if(!isReadonly) return result;
		
		if(RightsType.REQUIRED.getKey().equals(result) || RightsType.WRITE.getKey().equals(result)) {
			return RightsType.READ.getKey();
		}
		
		return result;
	}



}
