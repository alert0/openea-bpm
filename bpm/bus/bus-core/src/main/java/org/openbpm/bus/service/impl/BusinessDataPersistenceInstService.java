package org.openbpm.bus.service.impl;

import org.openbpm.bus.model.BusinessData;
import org.openbpm.bus.model.BusinessObject;
import org.springframework.stereotype.Service;

import org.openbpm.bus.api.constant.BusinessObjectPersistenceType;
import org.openbpm.bus.service.BusinessDataPersistenceService;


/**
 * <pre>
 * 实例表方式持久化业务数据的实现类
 * </pre>
 *
 *
 */
@Service
public class BusinessDataPersistenceInstService implements BusinessDataPersistenceService{

	@Override
	public String type() {
		return BusinessObjectPersistenceType.INST.getKey();
	}

	@Override
	public void saveData(BusinessData businessData) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public BusinessData loadData(BusinessObject businessObject, Object id) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void removeData(BusinessObject businessObject, Object id) {
		// TODO 自动生成的方法存根
		
	}



}
