package org.openbpm.demo.core.model;
import java.util.List;

import lombok.Data;
import org.openbpm.base.core.model.BaseModel;


/**
 * 案例 实体对象 (该对象与实体 Demo业务对象的json 直接转化)
 */
@Data
public class Demo extends BaseModel{
	/**
	* 名字
	*/
	protected  String mz; 
	/**
	* 爱好
	*/
	protected  String ah; 
	/**
	* 性别
	*/
	protected  String xb; 
	/**
	* 部门
	*/
	protected  String bm; 
	/**
	* 部门ID
	*/
	protected  String bmId; 
	/**
	* 证件类型
	*/
	protected  String zjlx; 
	/**
	* 年龄
	*/
	protected  Integer nl; 
	/**
	* 字段1
	*/
	protected  String zd1; 
	/**
	* 字段2
	*/
	protected  String zd2; 
	
	private List<DemoSub> demoSubList;

 
}