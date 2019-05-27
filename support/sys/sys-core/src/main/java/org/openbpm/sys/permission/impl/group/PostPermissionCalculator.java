package org.openbpm.sys.permission.impl.group;

import org.openbpm.sys.permission.impl.GroupPermissionCalculator;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * 描述：岗位
 * </pre>
 */
@Service
public class PostPermissionCalculator extends GroupPermissionCalculator {
	
	@Override
	public String getType() {
		return "post";
	}

	@Override
	public String getTitle() {
		return "岗位";
	}
}
