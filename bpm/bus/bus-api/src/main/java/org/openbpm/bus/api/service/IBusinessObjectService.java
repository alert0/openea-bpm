package org.openbpm.bus.api.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.bus.api.model.IBusinessObject;

/**
 * <pre>
 * 描述：Bo对其他模块提供出的service
 * </pre>
 */
public interface IBusinessObjectService {
	/**
	 * <pre>
	 * 根据key获取BusinessObject
	 * </pre>
	 * 
	 * @param key
	 * @return
	 */
	IBusinessObject getByKey(String key);

	/**
	 * <pre>
	 * 生成bo数据树
	 * 是pid那种形式
	 * </pre>
	 * 
	 * @param key
	 * @return
	 */
	List<JSONObject> boTreeData(String key);
	
	IBusinessObject getFilledByKey(String key);
	
}
