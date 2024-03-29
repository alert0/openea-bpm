package org.openbpm.bus.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import org.openbpm.base.core.model.BaseModel;
import org.openbpm.bus.api.model.IBusColumnCtrl;

/**
 * 字段对应的控件配置
 * 
 *
 * */
@Data
public class BusColumnCtrl extends BaseModel implements IBusColumnCtrl{
	@NotEmpty
	private String columnId; /* 对应字段的ID */
	@NotEmpty
	private String type; /* 控件类型  BusColumnCtrlType */
	private String config; /* 控件对应的配置内容，不同控件会有不同属性 */
	private String validRule; /* 验证规则 */

}