package org.openbpm.sys.service.impl;

import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.openbpm.sys.api.constant.RightsObjectConstants;
import org.openbpm.sys.api.service.SysAuthorizationService;
import org.openbpm.sys.core.manager.SysAuthorizationManager;
@Service
public class DefaultSysAuthorizationService implements SysAuthorizationService{
@Resource
SysAuthorizationManager sysAuthorizationManager;

	@Override
	public Set<String> getUserRights(String userId) {
		return sysAuthorizationManager.getUserRights(userId);
	}

	@Override
	public Map<String, Object> getUserRightsSql(RightsObjectConstants rightsObject, String userId, String targetKey) {
		return sysAuthorizationManager.getUserRightsSql(rightsObject, userId, targetKey);
	}

}
