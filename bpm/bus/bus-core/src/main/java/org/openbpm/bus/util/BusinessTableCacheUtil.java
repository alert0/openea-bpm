package org.openbpm.bus.util;

import java.util.HashMap;
import java.util.Map;

import org.openbpm.base.core.util.AppUtil;
import org.openbpm.bus.model.BusinessTable;

/**
 * <pre>
 * 描述：BusinessTable的缓存工具类
 * </pre>
 */
public class BusinessTableCacheUtil {
	private static String businessTableMap = "businessTableMap";

	private BusinessTableCacheUtil() {

	}

	public static void put(BusinessTable businessTable) {
		Map<String, BusinessTable> map = (Map<String, BusinessTable>) AppUtil.getCache().getByKey(businessTableMap);
		if (map == null) {
			map = new HashMap<>();
		}
		map.put(businessTable.getKey(), businessTable);
		AppUtil.getCache().add(businessTableMap, map);
	}

	public static BusinessTable get(String key) {
		Map<String, BusinessTable> map = (Map<String, BusinessTable>) AppUtil.getCache().getByKey(businessTableMap);
		if (map == null) {
			return null;
		}
		return map.get(key);
	}
}
