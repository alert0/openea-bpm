package org.openbpm.sys.rest.controller;



import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openbpm.sys.core.manager.HolidayConfManager;
import org.openbpm.sys.core.manager.WorkCalenDarManager;
import org.openbpm.sys.core.model.HolidayConf;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.api.exception.BusinessMessage;
import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.base.core.id.IdUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.rest.BaseController;
import org.openbpm.base.rest.util.RequestUtil;

@RestController
@RequestMapping("/calendar/holidayConf")
public class HolidayConfController extends BaseController<HolidayConf>{
	@Resource
    HolidayConfManager holidayConfManager;
	@Resource
    WorkCalenDarManager workCalenDarManager;

	
	/**
	 * 保存c_holiday_conf信息
	 * @param request
	 * @param response
	 * @param holidayConf
	 * @throws Exception 
	 * void
	 * @exception 
	 */
	@RequestMapping("save")
	@CatchErr("对节假日操作失败")
	@Override
	public ResultMsg<String> save(@RequestBody HolidayConf holidayConf) throws Exception{
		
		String id=holidayConf.getId();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(holidayConf.getStartDay());
		
		int year = calendar.get(Calendar.YEAR);
		holidayConf.setYear(year);
		
		
		if(StringUtil.isEmpty(id)){
			HolidayConf hc = holidayConfManager.queryOne(holidayConf.getName(),holidayConf.getStartDay(),holidayConf.getEndDay());
			if(hc != null)  throw new BusinessMessage("重复添加,相同日期内只能有一个同名节假日");
			
			holidayConf.setId(IdUtil.getSuid());
			workCalenDarManager.updateWhenHolidayConfCreate(holidayConf);
			holidayConfManager.create(holidayConf);
			return  getSuccessResult("添加节假日成功");
		}else{
			HolidayConf oldConf = holidayConfManager.get(id);
			workCalenDarManager.updateWhenHolidayConfUpd(oldConf, holidayConf);
			holidayConfManager.update(holidayConf);
			return  getSuccessResult("更新节假日成功"); 
		}
	}
	
	
	@RequestMapping("initWorkCalenDar")
	@CatchErr
	public ResultMsg<String> initWorkCalenDar(HttpServletRequest request,HttpServletResponse response){
		int year = RequestUtil.getInt(request, "year");
		if(year < 2000){
			throw new BusinessMessage("初始化日历信息，年份必须大于2000");
		}
	
		workCalenDarManager.initWorkCalenDarRecord(year);
		return getSuccessResult( "初始化"+ year + "年日历成功");
	}

	@Override
	protected String getModelDesc() {
		return "节假日";
	}
	

	@RequestMapping("test")
	@CatchErr("计算失败")
	public ResultMsg<Date> tset(@RequestBody HolidayConf holidayConf) throws Exception{
		Date startDay = holidayConf.getStartDay();
		Date endDay = holidayConf.getEndDay();
		Integer day = holidayConf.getYear();
		
		/*TODO if(endDay == null && day == null) {
			throw new BusinessMessage("结束日期与天数必须输入其中的一个");
		}
		
		StringBuilder sb = new StringBuilder();
		
		if(day == null) {
			if(startDay.after(endDay)){
				throw new BusinessMessage("开始日期不应该晚于结束日期", BaseStatusCode.PARAM_ILLEGAL);
			}
			List<WorkCalenDar> workCalenDars = workCalenDarManager.getByTime(startDay, endDay);
			sb.append("期间共[").append(workCalenDars.size()).append("]天：");
			
			workCalenDars.forEach(c ->{
				sb.append(c.getDay());
			});
		}*/
		Date d = workCalenDarManager.getWorkDayByDays(startDay,day);
		
		return getSuccessResult(d, "获取成功");
	}
	
	
	
	
}
