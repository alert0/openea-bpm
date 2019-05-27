package org.openbpm.demo.core.model;

import lombok.Data;
import org.openbpm.base.core.model.BaseModel;


/**
 * Demo子表 实体对象
 */
@Data
public class DemoSub extends BaseModel{
	/**
	* 名字
	*/
	protected  String mz; 
	/**
	* 描述
	*/
	protected  String ms; 
	/**
	* 字段1
	*/
	protected  String zd1; 
	/**
	* 字段2
	*/
	protected  String zd2; 
	/**
	* 外键
	*/
	protected  String fk; 

}