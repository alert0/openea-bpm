package org.openbpm.sys.core.model;

import lombok.Data;

@Data
public class ParticipantScheduleDO {
	/**
	* ID
	*/
	protected String id; 
	/**
	 * 业务关联id。此id用在回调时
	 */
	protected  String bizId;
	
	/**
	* 标题
	*/
	protected String title; 
	
	/**
	* 描述
	*/
	protected String remark; 
	
	/**
	* 任务连接
	*/
	protected String taskUrl; 
	
	/**
	* 类型
	*/
	protected String type; 
	
	/**
	* 任务打开方式
	*/
	protected String openType; 
	
	/**
	* 所属人
	*/
	protected String owner; 
	
	/**
	* 所属人
	*/
	protected String ownerName; 
	
	/**
	* 参与者
	*/
	protected String participantNames; 
	
	/**
	* 开始日期
	*/
	protected java.util.Date startTime; 
	
	/**
	* 结束日期
	*/
	protected java.util.Date endTime; 
	
	/**
	* 实际开始日期
	*/
	protected java.util.Date actualStartTime; 
	
	/**
	* 完成时间
	*/
	protected java.util.Date completeTime; 
	
	/**
	* 进度
	*/
	protected Integer rateProgress; 
	
	/**
	* 提交人
	*/
	protected String submitter; 
	
	/**
	* 提交人
	*/
	protected String submitNamer; 
	
	/**
	* isLock
	*/
	protected String isLock; 
	
	/**
	* 创建时间
	*/
	protected java.util.Date createTime; 
	
	/**
	* 创建人
	*/
	protected String createBy; 
	
	/**
	* 更新时间
	*/
	protected java.util.Date updateTime; 
	
	/**
	* 更新人
	*/
	protected String updateBy; 
	
	/**
	* 删除标记
	*/
	protected String deleteFlag; 
	
	
	/**
	* 日程ID
	*/
	protected String scheduleId; 
	
	/**
	* 参与者名字
	*/
	protected String participantorName; 
	
	/**
	* 参与者
	*/
	protected String participantor; 
	
	 
	/**
	* ilka提交注释
	*/
	protected String submitComment; 
	
	 
	
	/**
	* 版本
	*/
	protected Integer rev; 

	 
}
