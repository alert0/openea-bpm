package org.openbpm.bus.manager;

import org.openbpm.base.db.tableoper.TableOperator;
import org.openbpm.base.manager.Manager;
import org.openbpm.bus.model.BusinessTable;

/**
 * 
 *
 * */
public interface BusinessTableManager extends Manager<String, BusinessTable> {

	/**
	 * <pre>
	 * 保存businessTable
	 * 会同时处理 column的保存
	 * </pre>
	 * 
	 * @param businessTable
	 */
	void save(BusinessTable businessTable);

	/**
	 * <pre>
	 * 根据key获取businessTable
	 * </pre>
	 * 
	 * @param key
	 * @return
	 */
	BusinessTable getByKey(String key);

	/**
	 * <pre>
	 * 根据key获取装在好得businessTable
	 * </pre>
	 * 
	 * @param key
	 * @return
	 */
	BusinessTable getFilledByKey(String key);

	/**
	 * <pre>
	 * 根据businessTable获取表操作对象
	 * </pre>
	 * 
	 * @param businessTable
	 * @return
	 */
	TableOperator newTableOperator(BusinessTable businessTable);
	
	/**
	 * <pre>
	 * 根据businessTable获取表操作对象
	 * 并检查一下表是否存在，不存在报错
	 * </pre>
	 * 
	 * @param businessTable
	 * @return
	 */
	TableOperator newTableOperatorCheckExist(BusinessTable businessTable);
}
