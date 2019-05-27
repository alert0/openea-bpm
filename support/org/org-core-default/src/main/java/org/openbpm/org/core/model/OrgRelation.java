package org.openbpm.org.core.model;

import lombok.Data;
import org.openbpm.base.core.model.BaseModel;


/**
 * 用户组织关系 实体对象
 */
@Data
public class OrgRelation extends BaseModel{
	/**
	* 组ID
	*/
	protected  String groupId; 
	/**
	* 用户ID
	*/
	protected  String userId; 
	/**
	* 0:默认组织，1：从组织
	*/
	protected  Integer isMaster = 0; 
	
	/**
	* 0:默认组织，1：从组织
	*/
	protected  Integer status = 1; 
	
	/**
	* 角色ID
	*/
	protected  String roleId; 
	/**
	* 类型：groupUser,groupRole,userRole,groupUserRole
	*/
	protected  String type; 
	
	
	/**
	 * 前端字段
	 */
	protected String groupName;
	protected String userName;
	protected String roleName;
	protected String roleAlias;
	
	/**
	 * 岗位使用的时候调用
	 * @return
	 */
	public String getPostName() {
		return String.format("%s-%s", this.getGroupName(),this.getRoleName());
	}
	/**
	 * post ID
	 * @return
	 */
	public String getPostId() {
		return String.format("%s-%s", this.getGroupId(),this.getRoleId());
	}


	public void setGroupId( String groupId) {
		this.groupId = groupId;
	}
	

	public OrgRelation() {
		 
	}
	
	public OrgRelation(String groupId, String userId, String roleId, String type) {
		super();
		this.groupId = groupId;
		this.userId = userId;
		this.roleId = roleId;
		this.type = type;
	}

}