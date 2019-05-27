package org.openbpm.sys.core.manager;

import java.util.Date;

import org.openbpm.base.manager.Manager;
import org.openbpm.sys.core.model.HolidayConf;

/**
 * 
 * <pre> 
 * 描述：节假日配置 处理接口
 * </pre>
 */
public interface HolidayConfManager extends Manager<String, HolidayConf>{
	public HolidayConf queryOne(String name, Date startDay, Date endDay);
}
