package org.openbpm.bus.api.model;

/**
 * <pre>
 * 描述：BusTableRelFk接口类
 * </pre>
 */
public interface IBusTableRelFk {
	/**
	 * <pre>
	 * 业务表对应的映射字段
	 * </pre>
	 * 
	 * @return
	 */
	String getFrom();

	/**
	 * <pre>
	 * 映射的方式 枚举 BusTableRelFkType
	 * </pre>
	 * 
	 * @return
	 */
	String getType();

	/**
	 * <pre>
	 * 值
	 * </pre>
	 * 
	 * @return
	 */
	String getValue();
}
