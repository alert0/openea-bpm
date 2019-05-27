package org.openbpm.bus.manager;

import org.openbpm.base.manager.Manager;
import org.openbpm.bus.model.BusColumnCtrl;

/**
 * 
 *
 * */
public interface BusColumnCtrlManager extends Manager<String, BusColumnCtrl> {
	/**
	 * <pre>
	 * 根据tableId删除BusColCtrl
	 * </pre>
	 * @param tableId
	 */
	void removeByTableId(String tableId);
	
	/**
	 * <pre>
	 * 根据columnId获取BusColCtrl
	 * </pre>
	 * @param columnId
	 * @return
	 */
	BusColumnCtrl getByColumnId(String columnId);
}
