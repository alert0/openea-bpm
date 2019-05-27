package org.openbpm.bpm.plugin.global.taskskip.executer;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.engine.action.cmd.TaskActionCmd;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.openbpm.bpm.engine.constant.TaskSkipType;
import org.openbpm.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import org.openbpm.bpm.engine.plugin.session.BpmExecutionPluginSession;
import org.openbpm.bpm.plugin.global.taskskip.def.TaskSkipPluginDef;
import org.openbpm.sys.api.groovy.IGroovyScriptEngine;
import org.openbpm.sys.api.model.SysIdentity;
import org.openbpm.sys.util.ContextUtil;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class TaskSkipPluginExecutor extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, TaskSkipPluginDef> {
    @Resource
    BpmProcessDefService processDefService;
    @Resource
    IGroovyScriptEngine scriptEngine;
    

    public Void execute(BpmExecutionPluginSession pluginSession, TaskSkipPluginDef pluginDef) {
        ((DefualtTaskActionCmd)BpmContext.getActionModel()).setHasSkipThisTask(getSkipResult(pluginSession, pluginDef));
        return null;
    }

    private TaskSkipType getSkipResult(BpmExecutionPluginSession pluginSession, TaskSkipPluginDef pluginDef) {
        TaskActionCmd model = (TaskActionCmd) BpmContext.getActionModel();
        TaskSkipType skipResult = TaskSkipType.NO_SKIP;
        if (StringUtil.isEmpty(pluginDef.getSkipTypeArr())) {
            TaskSkipType taskSkipType = skipResult;
            return skipResult;
        }
        String[] skip = pluginDef.getSkipTypeArr().split(",");
        int length = skip.length;
        for (int i = 0; i < length; i++) {
            switch (skip[i]) {
                case "allSkip":
                    skipResult = TaskSkipType.ALL_SKIP;
                    break;
                case "scriptSkip":
                    if (this.scriptEngine.executeBoolean(pluginDef.getScript(), pluginSession)) {
                        skipResult = TaskSkipType.SCRIPT_SKIP;
                        break;
                    }
                    break;
                case "sameUserSkip":
                    List<SysIdentity> identityList = model.getBpmIdentity(model.getBpmTask().getNodeId());
                    if (!CollectionUtil.isEmpty(identityList) && identityList.size() <= 1) {
                        if (((SysIdentity) identityList.get(0)).getId().equals(ContextUtil.getCurrentUserId())) {
                            skipResult = TaskSkipType.SAME_USER_SKIP;
                            break;
                        }
                    } else {
                        TaskSkipType taskSkipType2 = skipResult;
                        return skipResult;
                    }
                    break;
                case "userEmptySkip":
                    if (CollectionUtil.isEmpty(model.getBpmIdentity(model.getBpmTask().getNodeId()))) {
                        skipResult = TaskSkipType.USER_EMPTY_SKIP;
                        break;
                    }
                    break;
                case "firstNodeSkip":
                    Iterator it = this.processDefService.getStartNodes(model.getBpmTask().getDefId()).iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (((BpmNodeDef) it.next()).getNodeId().equals(model.getBpmTask().getNodeId())) {
                                skipResult = TaskSkipType.FIRSTNODE_SKIP;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
            }
            if (skipResult != TaskSkipType.NO_SKIP) {
                this.LOG.info("{}节点【{}】设置了【{}】，将跳过当前任务", new Object[]{model.getBpmTask().getId(), model.getBpmTask().getName(), skipResult.getValue()});
                TaskSkipType taskSkipType3 = skipResult;
                return skipResult;
            }
        }
        TaskSkipType taskSkipType4 = skipResult;
        return skipResult;
    }
}
