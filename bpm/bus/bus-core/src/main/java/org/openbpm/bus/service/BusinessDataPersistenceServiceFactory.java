package org.openbpm.bus.service;

import java.util.Map;
import java.util.Map.Entry;

import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.bus.model.BusinessData;
import org.openbpm.bus.model.BusinessObject;

/**
 * <pre>
 * 描述：BusinessDataPersistenceService工厂类
 * </pre>
 */
public class BusinessDataPersistenceServiceFactory {
	private BusinessDataPersistenceServiceFactory() {

	}

	/**
	 * <pre>
	 * 获取指定类型的业务数据持久化的实现类
	 * </pre>
	 * 
	 * @param type
	 * @return
	 */
	private static BusinessDataPersistenceService getService(String type) {
		Map<String, BusinessDataPersistenceService> map = AppUtil.getImplInstance(BusinessDataPersistenceService.class);
		for (Entry<String, BusinessDataPersistenceService> entry : map.entrySet()) {
			if (entry.getValue().type().equals(type)) {
				return entry.getValue();
			}
		}
		throw new BusinessException(String.format("找不到类型[%s]的业务数据持久化的实现类", type));
	}

	/**
	 * <pre>
	 * 保存（持久化）businessData
	 * </pre>
	 * 
	 * @param businessData
	 */
	public static void saveData(BusinessData businessData) {
		BusinessObject businessObject = businessData.getBusTableRel().getBusObj();
		BusinessDataPersistenceService businessDataPersistenceService = getService(businessObject.getPersistenceType());
		businessDataPersistenceService.saveData(businessData);
	}

	/**
	 * <pre>
	 * 加载bo数据
	 * </pre>
	 * 
	 * @param boKey
	 * @param id
	 * @return
	 */
	public static BusinessData loadData(BusinessObject businessObject, Object id) {
		BusinessDataPersistenceService businessDataPersistenceService = getService(businessObject.getPersistenceType());
		return businessDataPersistenceService.loadData(businessObject, id);
	}

	/**
	 * <pre>
	 * 删除
	 * </pre>
	 * 
	 * @param boKey
	 * @param id
	 * @return
	 */
	public static void removeData(BusinessObject businessObject, Object id) {
		BusinessDataPersistenceService businessDataPersistenceService = getService(businessObject.getPersistenceType());
		businessDataPersistenceService.removeData(businessObject, id);
	}

}
