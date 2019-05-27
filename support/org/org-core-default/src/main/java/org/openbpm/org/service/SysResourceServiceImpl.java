package org.openbpm.org.service;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.openbpm.org.core.manager.ResRoleManager;
import org.openbpm.org.core.manager.SubsystemManager;
import org.openbpm.org.core.manager.SysResourceManager;
import org.springframework.stereotype.Service;

import org.openbpm.org.api.model.system.ISubsystem;
import org.openbpm.org.api.model.system.ISysResource;
import org.openbpm.org.api.service.SysResourceService;

/**
 * 用户系统资源服务接口
 */
@Service
public class SysResourceServiceImpl implements SysResourceService{
	@Resource
	SysResourceManager sysResourceManager;
	@Resource
	SubsystemManager sybSystemManager;
	@Resource
	ResRoleManager resRoleManager;
	
	
	@Override
	public List<ISubsystem> getCuurentUserSystem() {
		return (List)sybSystemManager.getCuurentUserSystem();
	}

	@Override
	public ISubsystem getDefaultSystem(String currentUserId) {
		return sybSystemManager.getDefaultSystem(currentUserId);
	}

	@Override
	public List<ISysResource> getBySystemId(String systemId) {
		return (List)sysResourceManager.getBySystemId(systemId);
	}

	@Override
	public List<ISysResource> getBySystemAndUser(String systemId, String userId) {
		return (List)sysResourceManager.getBySystemAndUser(systemId, userId);
	}


	@Override
	public Set<String> getAccessRoleByUrl(String url) {
		return resRoleManager.getAccessRoleByUrl(url);
	}

}
