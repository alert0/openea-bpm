package org.openbpm.sys.api.service;

import java.util.List;

import org.openbpm.org.api.model.IUser;
import org.openbpm.sys.api.model.SysIdentity;

public interface SysIdentityConvert {
	
	/**
	 *  identity type 必须为 user
	 * @param identity
	 * @return
	 */
	public IUser convert2User(SysIdentity identity);
	
	public List<? extends IUser> convert2Users(SysIdentity identity);
	
	public List<? extends IUser> convert2Users(List<SysIdentity> identity);
	
}
