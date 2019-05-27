package org.openbpm.sys.permission.impl.group;

import org.openbpm.sys.permission.impl.GroupPermissionCalculator;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * 描述：角色
 * </pre>
 */
@Service
public class RolePermissionCalculator extends GroupPermissionCalculator {
	
	@Override
	public String getType() {
		return "role";
	}

	@Override
	public String getTitle() {
		return "角色";
	}
}
