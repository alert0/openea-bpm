package org.openbpm.bus.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.openbpm.base.core.util.AppUtil;

/**
 * <pre>
 * 描述：BusinessObject的缓存工具类
 * </pre>
 */
public class BusinessObjectCacheUtil {
	private static final String BUSINESS_OBJECT_DATASOURCE_KEYS_MAP = "businessObjectDataSourceKeysMap";

	private BusinessObjectCacheUtil() {

	}
	
	/**
	 * <pre>
	 * 缓存bo的数据源别名set
	 * </pre>	
	 * @param boKey
	 * @param dsKeys
	 */
	public static void putDataSourcesKeys(String boKey, Set<String> dsKeys) {
		Map<String, Set<String>> map = (Map<String, Set<String>>) AppUtil.getCache().getByKey(BUSINESS_OBJECT_DATASOURCE_KEYS_MAP);
		if (map == null) {
			map = new HashMap<>();
		}
		map.put(boKey, dsKeys);
		AppUtil.getCache().add(BUSINESS_OBJECT_DATASOURCE_KEYS_MAP, map);
	}
	
	/**
	 * <pre>
	 * 拿出缓存中bo数据源别名set
	 * </pre>	
	 * @param boKey
	 * @return
	 */
	public static Set<String> getDataSourcesKeys(String boKey) {
		Map<String, Set<String>> map = (Map<String, Set<String>>) AppUtil.getCache().getByKey(BUSINESS_OBJECT_DATASOURCE_KEYS_MAP);
		if (map == null) {
			return null;
		}
		return map.get(boKey);
	}
}
