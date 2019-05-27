package org.openbpm.bpm.plugin.node.userassign.executer;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import org.openbpm.bpm.api.engine.action.cmd.TaskActionCmd;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.engine.plugin.def.UserAssignRule;
import org.openbpm.bpm.engine.plugin.factory.BpmPluginSessionFactory;
import org.openbpm.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import org.openbpm.bpm.engine.plugin.session.BpmExecutionPluginSession;
import org.openbpm.bpm.plugin.node.userassign.def.UserAssignPluginDef;
import org.openbpm.sys.api.groovy.IGroovyScriptEngine;
import org.openbpm.sys.api.model.SysIdentity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class UserAssignPluginExecutor extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, UserAssignPluginDef> {
    @Resource
    IGroovyScriptEngine groovyScriptEngine;

    public Void execute(BpmExecutionPluginSession pluginSession, UserAssignPluginDef assignPluginDef) {
        TaskActionCmd model = (TaskActionCmd) BpmContext.getActionModel();
        if (!CollectionUtil.isNotEmpty(model.getBpmIdentity(model.getNodeId()))) {
            List<UserAssignRule> ruleList = assignPluginDef.getRuleList();
            if (!CollectionUtil.isEmpty(ruleList)) {
                List<SysIdentity> bpmIdentities = UserAssignRuleCalc.calcUserAssign(BpmPluginSessionFactory.buildBpmUserCalcPluginSession(pluginSession), ruleList, Boolean.valueOf(false));
                List<SysIdentity> identitieList = new ArrayList<>();
                for (SysIdentity identity : bpmIdentities) {
                    if (identity != null) {
                        identitieList.add(identity);
                    }
                }
                this.LOG.debug("用户计算插件执行完毕，解析到【{}】条有效人员信息。节点:{}", Integer.valueOf(identitieList.size()), model.getNodeId());
                this.LOG.trace(JSON.toJSONString(identitieList));
                model.setBpmIdentity(model.getNodeId(), identitieList);
            }
        }
        return null;
    }
}
