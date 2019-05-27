package org.openbpm.bpm.plugin.usercalc.group.executer;

import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.engine.model.BpmIdentity;
import org.openbpm.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
import org.openbpm.bpm.engine.plugin.session.BpmUserCalcPluginSession;
import org.openbpm.bpm.plugin.usercalc.group.def.GroupPluginDef;
import org.openbpm.org.api.model.IGroup;
import org.openbpm.org.api.service.GroupService;
import org.openbpm.sys.api.model.SysIdentity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class GroupPluginExecutor extends AbstractUserCalcPlugin<GroupPluginDef> {
    @Resource
    GroupService userGroupService;

    public List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, GroupPluginDef def) {
        String[] split;
        if (StringUtil.isEmpty(def.getGroupKey())) {
            return null;
        }
        String groupType = def.getType();
        List<SysIdentity> identityList = new ArrayList<>();
        for (String key : def.getGroupKey().split(",")) {
            if (!StringUtil.isEmpty(key)) {
                IGroup group = this.userGroupService.getByCode(groupType, key);
                if (group != null) {
                    identityList.add(new BpmIdentity(group.getGroupId(), group.getGroupName(), group.getGroupType()));
                }
            }
        }
        return identityList;
    }
}
