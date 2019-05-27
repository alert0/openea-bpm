package org.openbpm.bus.service;

import org.openbpm.bus.manager.BusinessTableManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.openbpm.bus.api.model.IBusinessTable;
import org.openbpm.bus.api.service.IBusinessTableService;

/**
 * <pre>
 * 描述：IBusinessTableService实现类
 * </pre>
 */
@Service
public class BusinessTableService implements IBusinessTableService {
	@Autowired
	BusinessTableManager businessTableManager;

	@Override
	public IBusinessTable getByKey(String key) {
		return businessTableManager.getByKey(key);
	}

	@Override
	public IBusinessTable getFilledByKey(String key) {
		return businessTableManager.getFilledByKey(key);
	}

}
