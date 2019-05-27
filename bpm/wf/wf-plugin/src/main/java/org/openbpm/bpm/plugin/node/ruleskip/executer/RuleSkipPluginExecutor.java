package org.openbpm.bpm.plugin.node.ruleskip.executer;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.engine.action.cmd.TaskActionCmd;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import org.openbpm.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
import org.openbpm.bpm.plugin.node.ruleskip.def.JumpRule;
import org.openbpm.bpm.plugin.node.ruleskip.def.RuleSkipPluginDef;
import org.openbpm.sys.api.groovy.IGroovyScriptEngine;
import java.util.Iterator;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class RuleSkipPluginExecutor extends AbstractBpmExecutionPlugin<DefaultBpmTaskPluginSession, RuleSkipPluginDef> {
    @Resource
    IGroovyScriptEngine scriptEngine;

    public Void execute(DefaultBpmTaskPluginSession pluginSession, RuleSkipPluginDef pluginDef) {
        if (!CollectionUtil.isEmpty(pluginDef.getJumpRules())) {
            TaskActionCmd taskAction = (TaskActionCmd) BpmContext.getActionModel();
            if (!StringUtil.isNotEmpty(taskAction.getDestination())) {
                Iterator it = pluginDef.getJumpRules().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        this.LOG.info("节点{}规则跳转，共{}条，均不符合条件，将正常跳转", pluginSession.getBpmTask().getName(), Integer.valueOf(pluginDef.getJumpRules().size()));
                        break;
                    }
                    JumpRule jumpRule = (JumpRule) it.next();
                    if (!StringUtil.isEmpty(jumpRule.getScript()) && !StringUtil.isEmpty(jumpRule.getScript()) && this.scriptEngine.executeBoolean(jumpRule.getScript(), pluginSession)) {
                        taskAction.setDestination(jumpRule.getTargetNode());
                        this.LOG.info("节点【{}】规则跳转【{}】条件满足，即将跳转至【{}】", new Object[]{pluginSession.getBpmTask().getName(), jumpRule.getName(), jumpRule.getTargetNode()});
                        this.LOG.debug(jumpRule.getScript());
                        break;
                    }
                }
            } else {
                this.LOG.info("任务【{}】已经指定了跳转节点【{}】，规则跳转将忽略", pluginSession.getBpmTask().getName(), taskAction.getDestination());
            }
        }
        return null;
    }
}
