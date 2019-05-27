package org.openbpm.sys.core.manager.impl;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.openbpm.sys.core.dao.LogErrDao;
import org.openbpm.sys.core.manager.LogErrManager;
import org.openbpm.sys.core.model.LogErr;
import org.springframework.stereotype.Service;

import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.base.rest.util.IPAddressUtil;

/**
 * <pre>
 * 描述：错误日志 处理实现类
 * </pre>
 */
@Service("sysLogErrManager")
public class LogErrManagerImpl extends BaseManager<String, LogErr> implements LogErrManager {
    @Resource
    LogErrDao sysLogErrDao;

    
    @Override
    public void create(LogErr entity) {
    	String ip = entity.getIp();
    	if(StringUtil.isNotEmpty(ip)) {
    		try {
    			entity.setIpAddress(IPAddressUtil.getAddresses(ip));
			} catch (UnsupportedEncodingException e) {
			}
    	}
    	
    	sysLogErrDao.create(entity);
    }
    
    
    
}
