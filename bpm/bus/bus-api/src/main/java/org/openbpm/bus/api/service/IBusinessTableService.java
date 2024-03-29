package org.openbpm.bus.api.service;

import org.openbpm.bus.api.model.IBusinessTable;

/**
 * <pre>
 * 描述：业务表对其他模块的service
 * </pre>
 */
public interface IBusinessTableService {
	/**
	 * <pre>
	 * 根据key获取businessTable
	 * </pre>
	 * 
	 * @param key
	 * @return
	 */
	IBusinessTable getByKey(String key);
	
	/**
	 * <pre>
	 * 获取填充好所有数据的表
	 * </pre>
	 * @param key
	 * @return
	 */
	IBusinessTable getFilledByKey(String key);
	
}
