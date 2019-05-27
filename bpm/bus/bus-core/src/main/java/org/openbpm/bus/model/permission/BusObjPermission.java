package org.openbpm.bus.model.permission;

import java.util.HashMap;
import java.util.Map;

import org.openbpm.bus.api.model.permission.IBusObjPermission;

/**
 * <pre>
 * 描述：bo权限
 * </pre>
 */
public class BusObjPermission extends AbstractPermission implements IBusObjPermission{
	/**
	 * boKey
	 */
	private String key;
	/**
	 * <pre>
	 * boName
	 * </pre>
	 */
	private String name;
	/**
	 * <pre>
	 * 权限map
	 * Map<tableKey,BusTablePermission>
	 * </pre>
	 */
	private Map<String, BusTablePermission> tableMap = new HashMap<>();
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, BusTablePermission> getTableMap() {
		return tableMap;
	}

	public void setTableMap(Map<String, BusTablePermission> tableMap) {
		this.tableMap = tableMap;
	}

}
