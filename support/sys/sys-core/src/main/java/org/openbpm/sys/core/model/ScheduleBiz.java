package org.openbpm.sys.core.model;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.openbpm.base.api.model.IDModel;


 /**
  * 日程业务关联表 实体对象
 */
 @Data
public class ScheduleBiz implements IDModel{
	
	/**
	* id
	*/
	protected String id; 
	
	/**
	* 日程id
	*/
	protected String scheduleId; 
	
	/**
	* 业务id
	*/
	protected String bizId; 
	
	/**
	* 来源
	*/
	protected String source; 
	

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("scheduleId", this.scheduleId) 
		.append("bizId", this.bizId) 
		.append("source", this.source) 
		.toString();
	}
}