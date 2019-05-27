package org.openbpm.sys.permission.impl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.base.core.util.ThreadMapUtil;
import org.openbpm.org.api.model.IGroup;
import org.openbpm.org.api.service.GroupService;
import org.openbpm.sys.api.permission.IPermissionCalculator;
import org.openbpm.sys.util.ContextUtil;
/**
 * <pre>
 * 描述：组 抽象类
 * </pre>
 */
public abstract class GroupPermissionCalculator implements IPermissionCalculator {
	/**
	 * 线程变量ThreadMapUtil中关于当前权限解析器的线程变量
	 */
	private static String threadMapKey = "org.openbpm.sys.permission.impl.GroupPermission";
	
	@Override
	public boolean haveRights(JSONObject json) {
		Map<String, List<? extends IGroup>> allGroup = (Map<String, List<? extends IGroup>>) ThreadMapUtil.get(threadMapKey);
		if(allGroup ==null) {
			GroupService groupService = AppUtil.getBean(GroupService.class);
			allGroup = groupService.getAllGroupByUserId(ContextUtil.getCurrentUserId());
			ThreadMapUtil.put(threadMapKey, allGroup);
		}
		
		List<? extends IGroup> groups;
		if("post".equals(this.getType())) {//岗位的命名不一致
			groups = allGroup.get("position");
		}else {
			groups = allGroup.get(this.getType());
		}
		
		for(IGroup group:groups) {
			if(json.getString("id").contains(group.getGroupId())) {
				return true;
			}
		}
		
		return false;
	}
	
}
