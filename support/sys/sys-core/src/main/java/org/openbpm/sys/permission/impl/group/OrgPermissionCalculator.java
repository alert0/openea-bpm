package org.openbpm.sys.permission.impl.group;

import org.openbpm.sys.permission.impl.GroupPermissionCalculator;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * 描述：组织
 * </pre>
 */
@Service
public class OrgPermissionCalculator extends GroupPermissionCalculator {
	
	@Override
	public String getType() {
		return "org";
	}

	@Override
	public String getTitle() {
		return "组织";
	}
	
}
