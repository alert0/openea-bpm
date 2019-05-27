package org.openbpm.bus.manager;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.manager.Manager;
import org.openbpm.bus.model.BusinessObject;

/**
 * <pre>
 * 描述：BusinessObject Manager层
 * </pre>
 */
public interface BusinessObjectManager extends Manager<String, BusinessObject> {
	/**
	 * <pre>
	 * 根据key获取BusinessObject
	 * </pre>
	 * 
	 * @param key
	 * @return
	 */
	BusinessObject getByKey(String key);

	/**
	 * <pre>
	 * boTree的数据
	 * </pre>
	 * 
	 * @param key
	 * @return
	 */
	List<JSONObject> boTreeData(String key);
	
	/**
	 * <pre>
	 * 根据key获取填充好的BusinessObject
	 * rel包含table对象 table中的ctrl也被填充好
	 * </pre>
	 * @param key
	 * @return
	 */
	BusinessObject getFilledByKey(String key);
}
