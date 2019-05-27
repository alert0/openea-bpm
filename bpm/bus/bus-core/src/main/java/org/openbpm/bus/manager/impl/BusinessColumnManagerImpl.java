package org.openbpm.bus.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.bus.dao.BusinessColumnDao;
import org.openbpm.bus.manager.BusinessColumnManager;
import org.openbpm.bus.model.BusinessColumn;

/**
 * businessColumn 的manager层实现类
 *
 */
@Service
public class BusinessColumnManagerImpl extends BaseManager<String, BusinessColumn> implements BusinessColumnManager {
	@Resource
	BusinessColumnDao businessColumnDao;

	@Override
	public void removeByTableId(String tableId){
		businessColumnDao.removeByTableId(tableId);
	}

	@Override
	public List<BusinessColumn> getByTableId(String tableId) {
		return businessColumnDao.getByTableId(tableId);
	}
}
