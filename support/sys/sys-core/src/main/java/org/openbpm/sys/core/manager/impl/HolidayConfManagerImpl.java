package org.openbpm.sys.core.manager.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.openbpm.sys.core.dao.HolidayConfDao;
import org.openbpm.sys.core.manager.WorkCalenDarManager;
import org.openbpm.sys.core.model.HolidayConf;
import org.springframework.stereotype.Service;

import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.sys.core.manager.HolidayConfManager;

/**
 * 
 * <pre> 
 * 描述：c_holiday_conf 处理实现类
 * 作者:miao
 * 邮箱:miaojifang@ddjf.com.cn
 * 日期:2017-12-26 11:52:16
 * </pre>
 */
@Service("holidayConfManager")
public class HolidayConfManagerImpl extends BaseManager<String, HolidayConf> implements HolidayConfManager{
	@Resource
    HolidayConfDao holidayConfDao;
	@Resource
    WorkCalenDarManager workCalenDarManager;
	 
	@Override
	public HolidayConf queryOne(String name, Date startDay, Date endDay) {
		return holidayConfDao.queryOne(name, startDay, endDay);
	}
	
	
	@Override
	public void removeByIds(String... ids) {
		for(String id : ids) {
			HolidayConf conf = this.get(id);
			workCalenDarManager.clearWorkCalenDarByHoliday(conf);
			this.remove(id);
		}
	}
}
