package org.openbpm.org.core.model;
import java.util.List;

import lombok.Data;
import org.openbpm.base.core.model.BaseModel;
import org.openbpm.org.api.constant.GroupTypeConstant;
import org.openbpm.org.api.model.IGroup;


/**
 * 组 实体对象
 */

@Data
public class Group extends BaseModel implements IGroup{
	/**
	* 名字
	*/
	protected  String name; 
	/**
	* 父ID
	*/
	protected  String parentId; 
	/**
	* 编码
	*/
	protected  String code; 
	/**
	* 类型
	*/
	protected  Integer type; 
	/**
	* 描述
	*/
	protected  String desc; 
	/**
	* path_
	*/
	protected  String path; 
	/**
	* 排序
	*/
	protected  Integer sn; 
	
	/**
	 * 岗位
	 */
	protected List<OrgRelation> orgRelationList;
	
	/**
	 *  前端字段
	 */
	protected String parentName;
	
	
	

	@Override
	public String getGroupId() {
		return this.id;
	}

	@Override
	public String getGroupCode() {
		return this.code;
	}

 
	@Override
	public String getGroupType() {
		return GroupTypeConstant.ORG.key();
	}

	@Override
	public String getGroupName() {
		return this.name;
	}
 
}