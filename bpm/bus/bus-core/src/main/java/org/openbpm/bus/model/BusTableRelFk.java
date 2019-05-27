package org.openbpm.bus.model;

import java.io.Serializable;

import lombok.Data;
import org.openbpm.bus.api.model.IBusTableRelFk;

/**
 * <pre>
 * BusTabRelation中的外键实体类
 * </pre>
 *
 *
 */
@Data
public class BusTableRelFk implements Serializable, IBusTableRelFk {
	/**
	 * 业务表对应的映射字段
	 */
	private String from;
	/**
	 * 映射的方式 枚举 BusTableRelFkType
	 */
	private String type;
	/**
	 * 值
	 */
	private String value;

}
