package org.openbpm.sys.core.model;

import lombok.Data;
import org.openbpm.base.core.model.BaseModel;


/**
 * 数据字典 实体对象
 */
@Data
public class DataDict extends BaseModel{
	public static final String TYPE_DICT = "dict";
	public static final String TYPE_NODE = "node";
	/**
	* 上级id
	*/
	protected  String parentId ; 
	/**
	* key
	*/
	protected  String key; 
	/**
	* name
	*/
	protected  String name; 
	/**
	* 字典key
	*/
	protected  String dictKey; 
	/**
	* 分组id
	*/
	protected  String typeId; 
	/**
	* 排序
	*/
	protected  Integer sn = 0; 
	/**
	* dict/node字典项
	*/
	protected  String dictType; 
	/**
	* 是否删除
	*/
	protected  String deleteFlag = "0";
 
}