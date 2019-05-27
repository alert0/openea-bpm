package org.openbpm.bus.service;

import java.util.List;

import org.openbpm.bus.manager.BusinessObjectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.bus.api.model.IBusinessObject;
import org.openbpm.bus.api.service.IBusinessObjectService;

/**
 * <pre>
 *  
 * 描述：IBusinessObjectService实现类
 * </pre>
 */
@Service
public class BusinessObjectService implements IBusinessObjectService {
	@Autowired
	BusinessObjectManager businessObjectManager;

	@Override
	public IBusinessObject getByKey(String key) {
		return businessObjectManager.getByKey(key);
	}

	@Override
	public IBusinessObject getFilledByKey(String key) {
		return businessObjectManager.getFilledByKey(key);
	}

	@Override
	public List<JSONObject> boTreeData(String key) {
		return businessObjectManager.boTreeData(key);
	}

}
