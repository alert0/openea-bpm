package org.openbpm.sys.groovy;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import org.openbpm.org.api.model.IGroup;
import org.openbpm.org.api.model.IUser;
import org.openbpm.sys.api.groovy.IScript;
import org.openbpm.sys.api.service.SerialNoService;
import org.openbpm.sys.util.ContextUtil;

/**
 * 系统脚本
 * 常用的系统功能的脚本
 */
@Component
public class SysScript implements IScript {
	@Resource
	SerialNoService serialNoService;
	
	/**
	 * 获取系统流水号
	 * @param alias
	 * @return
	 */
	public String getNextSerialNo(String alias) {
		return serialNoService.genNextNo(alias);
	}
	
	
	public IUser getCurrentUser() {
		IUser user = ContextUtil.getCurrentUser();
		return user;
	}
	
	public String getCurrentGroupName() {
		 IGroup iGroup =ContextUtil.getCurrentGroup();
        if (iGroup!= null) {
            return iGroup.getGroupName();
        } else {
            return "";
        }
	}
	
	public String getCurrentUserName() {
		return ContextUtil.getCurrentUser().getFullname();
	}
	
}
