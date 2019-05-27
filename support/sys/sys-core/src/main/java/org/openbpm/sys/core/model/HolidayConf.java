package org.openbpm.sys.core.model;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import org.openbpm.base.api.model.IDModel;


 /**
 * 
 * 节假日配置
 */
 @Data
public class HolidayConf implements IDModel{
	
	/**
	* id
	*/
	protected String id; 
	
	/**
	* 名字
	*/
	@NotBlank
	protected String name; 
	
	/**
	* 系统
	*/
	protected String system; 
	
	/**
	* 年份
	*/
	@NotEmpty
	protected Integer year; 
	
	/**
	* 开始日期
	*/
	@NotEmpty
	protected java.util.Date startDay; 
	
	/**
	* 结束日期
	*/
	@NotEmpty
	protected java.util.Date endDay; 
	
	/**
	* 分类
	*/
	@NotBlank
	protected String type; 
	
	/**
	* 描述
	*/
	protected String remark; 
	
	

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("system", this.system) 
		.append("year", this.year) 
		.append("startDay", this.startDay) 
		.append("endDay", this.endDay) 
		.append("type", this.type) 
		.append("remark", this.remark) 
		.toString();
	}
}