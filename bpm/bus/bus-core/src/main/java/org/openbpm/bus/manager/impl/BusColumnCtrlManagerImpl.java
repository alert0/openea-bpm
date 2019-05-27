package org.openbpm.bus.manager.impl;

import javax.annotation.Resource;

import org.openbpm.bus.manager.BusColumnCtrlManager;
import org.springframework.stereotype.Service;

import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.bus.dao.BusColumnCtrlDao;
import org.openbpm.bus.model.BusColumnCtrl;

/**
 * busColCtrl 的manager层实现类
 *
 */
@Service
public class BusColumnCtrlManagerImpl extends BaseManager<String, BusColumnCtrl> implements BusColumnCtrlManager {
	@Resource
	BusColumnCtrlDao busColumnCtrlDao;

	@Override
	public void removeByTableId(String tableId) {
		busColumnCtrlDao.removeByTableId(tableId);
	}

	@Override
	public BusColumnCtrl getByColumnId(String columnId) {
		return busColumnCtrlDao.getByColumnId(columnId);
	}

}
