package org.openbpm.bus.model.permission;

import org.openbpm.bus.api.model.permission.IBusColumnPermission;

/**
 * <pre>
 *  
 * 描述：字段权限
 * </pre>
 */
public class BusColumnPermission extends AbstractPermission implements IBusColumnPermission {
	/**
	 * 字段名
	 */
	private String key;
	/**
	 * 字段描述
	 */
	private String comment;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
