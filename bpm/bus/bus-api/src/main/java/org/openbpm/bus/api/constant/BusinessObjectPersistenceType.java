package org.openbpm.bus.api.constant;
/**
 * <pre> 
 * 描述：BusinessObject中PersistenceType属性的枚举
 * </pre>
 */
public enum BusinessObjectPersistenceType {
	/**
	 * 数据库
	 */
	DB("db", "数据库"), 
	/**
	 * 实例表
	 */
	INST("inst", "实例表-暂不支持");
	private String key;
	private String desc;

	private BusinessObjectPersistenceType(String key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	public String getKey() {
		return key;
	}

	public String getDesc() {
		return desc;
	}
	
	/**
	 * <pre>
	 * 根据key来判断是否跟当前一致
	 * </pre>
	 * @param key
	 * @return
	 */
	public boolean equalsWithKey(String key) {
		return this.key.equals(key);
	}
}
