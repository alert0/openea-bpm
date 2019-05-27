package org.openbpm.sys.core.manager;

import java.util.Date;
import java.util.List;

import org.openbpm.base.manager.Manager;
import org.openbpm.sys.api.model.calendar.Schedule;
import org.openbpm.sys.core.model.ParticipantScheduleDO;

/**
 * 
 * 描述：日程 处理接口
 */
public interface ScheduleManager extends Manager<String, Schedule>{
	
	public List<Schedule> getByPeriodAndOwner(Date start, Date end, String ownerName, String owner);
	
	public void saveSchedule(Schedule schedule);
	
	public List<Schedule> getByPeriodAndSource(Date start, Date end, String source);
	
	public void deleteByBizId(String bizId);
	
	public void dragUpdate(Schedule schedule);
	
	public void updateSchedule(String biz_id, Date start, Date end, String ownerAccount);
	
	public List<Schedule> getByBizId(String biz_id);
	
	public void updateOnlySchedule(Schedule schedule);

	public List<ParticipantScheduleDO> getParticipantEvents(Date startDate, Date endDate, String name, String id);
	
}
