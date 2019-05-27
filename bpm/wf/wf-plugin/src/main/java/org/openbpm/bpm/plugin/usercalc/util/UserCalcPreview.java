package org.openbpm.bpm.plugin.usercalc.util;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.engine.plugin.context.BpmPluginContext;
import org.openbpm.bpm.api.engine.plugin.def.BpmPluginDef;
import org.openbpm.bpm.api.engine.plugin.def.UserAssignRule;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.openbpm.bpm.engine.plugin.factory.BpmPluginSessionFactory;
import org.openbpm.bpm.engine.plugin.session.BpmExecutionPluginSession;
import org.openbpm.bpm.plugin.node.userassign.def.UserAssignPluginDef;
import org.openbpm.bpm.plugin.node.userassign.executer.UserAssignRuleCalc;
import org.openbpm.sys.api.model.SysIdentity;
import java.util.Collections;
import java.util.List;

public class UserCalcPreview {
    public static List<SysIdentity> calcNodeUsers(BpmNodeDef userNode, DefualtTaskActionCmd taskModel) {
        for (BpmPluginContext bpmPluginContext : userNode.getBpmPluginContexts()) {
            BpmPluginDef bpmPluginDef = bpmPluginContext.getBpmPluginDef();
            if (bpmPluginDef instanceof UserAssignPluginDef) {
                UserAssignPluginDef userAssignPluginDef = (UserAssignPluginDef) bpmPluginDef;
                BpmExecutionPluginSession bpmTaskSession = BpmPluginSessionFactory.buildTaskPluginSession(taskModel, EventType.TASK_COMPLETE_EVENT);
                List<UserAssignRule> ruleList = userAssignPluginDef.getRuleList();
                if (!CollectionUtil.isEmpty(ruleList)) {
                    return UserAssignRuleCalc.calcUserAssign(BpmPluginSessionFactory.buildBpmUserCalcPluginSession(bpmTaskSession), ruleList, Boolean.valueOf(true));
                }
            }
        }
        return Collections.emptyList();
    }
}
