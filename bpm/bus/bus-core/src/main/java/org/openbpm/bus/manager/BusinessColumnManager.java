package org.openbpm.bus.manager;

import java.util.List;

import org.openbpm.base.manager.Manager;
import org.openbpm.bus.model.BusinessColumn;

/**
 * 
 *
 * */
public interface BusinessColumnManager extends Manager<String, BusinessColumn> {
	/**
	 * <pre>
	 * 根据tableId删除字段
	 * </pre>
	 * 
	 * @param tableId
	 */
	void removeByTableId(String tableId);

	/**
	 * <pre>
	 * 根据tableId获取字段
	 * </pre>
	 * 
	 * @param tableId
	 * @return
	 */
	List<BusinessColumn> getByTableId(String tableId);
}
