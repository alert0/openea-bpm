package org.openbpm.sys.core.dao;
import java.util.Date;

import org.apache.ibatis.annotations.Param;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.sys.core.model.HolidayConf;

public interface HolidayConfDao extends BaseDao<String, HolidayConf> {
	public HolidayConf queryOne(@Param("name")String name,@Param("startDay") Date startDay, @Param("endDay")Date endDay);
}
