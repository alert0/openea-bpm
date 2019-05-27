package org.openbpm.bpm.engine.plugin.cmd;

import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.engine.action.cmd.TaskActionCmd;
import org.openbpm.bpm.api.engine.plugin.cmd.TaskCommand;
import org.openbpm.bpm.api.engine.plugin.context.BpmPluginContext;
import org.openbpm.bpm.api.engine.plugin.def.BpmExecutionPluginDef;
import org.openbpm.bpm.api.engine.plugin.def.BpmPluginDef;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import org.openbpm.bpm.engine.plugin.factory.BpmPluginFactory;
import org.openbpm.bpm.engine.plugin.factory.BpmPluginSessionFactory;
import org.openbpm.bpm.engine.plugin.runtime.BpmExecutionPlugin;
import org.openbpm.bpm.engine.plugin.session.BpmExecutionPluginSession;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TaskPluginCommand implements TaskCommand {
    protected Logger LOG = LoggerFactory.getLogger(getClass());
    @Resource
    BpmProcessDefService bpmProcessDefService;

    public void execute(EventType eventType, TaskActionCmd actionModel) {
        this.LOG.debug("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓【{}】插件执行开始↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓", eventType.getValue());
        String defId = actionModel.getBpmTask().getDefId();
        BpmNodeDef bpmNodeDef = this.bpmProcessDefService.getBpmNodeDef(defId, actionModel.getNodeId());
        BpmExecutionPluginSession bpmTaskSession = BpmPluginSessionFactory.buildTaskPluginSession(actionModel, eventType);
        BpmExecutionPluginSession executionSession = BpmPluginSessionFactory.buildExecutionPluginSession(actionModel, eventType);
        this.LOG.trace("============【节点插件】========{}=========", Integer.valueOf(bpmNodeDef.getBpmPluginContexts().size()));
        for (BpmPluginContext bpmPluginContext : bpmNodeDef.getBpmPluginContexts()) {
            BpmPluginDef bpmPluginDef = bpmPluginContext.getBpmPluginDef();
            if (bpmPluginDef instanceof BpmExecutionPluginDef) {
                BpmExecutionPlugin bpmTaskPlugin = BpmPluginFactory.buildExecutionPlugin(bpmPluginContext, eventType);
                if (bpmTaskPlugin != null) {
                    try {
                        this.LOG.debug("==>执行任务插件【{}】", bpmPluginContext.getTitle());
                        bpmTaskPlugin.execute(bpmTaskSession, bpmPluginDef);
                    } catch (Exception e) {
                        this.LOG.error("{}执行任务插件【{}】出现异常:{}", new Object[]{actionModel.getNodeId(), bpmPluginContext.getTitle(), e.getMessage(), e});
                        throw new BusinessException(bpmPluginContext.getTitle(), BpmStatusCode.PLUGIN_ERROR, e);
                    }
                }
            }
        }
        DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(defId);
        this.LOG.trace("============【全局插件】========{}=========", Integer.valueOf(bpmProcessDef.getBpmPluginContexts().size()));
        for (BpmPluginContext globalCtx : bpmProcessDef.getBpmPluginContexts()) {
            BpmExecutionPlugin bpmExecutionPlugin = BpmPluginFactory.buildExecutionPlugin(globalCtx, eventType);
            if (bpmExecutionPlugin != null) {
                try {
                    this.LOG.debug("==>【{}】节点执行全局插件【{}】", bpmNodeDef.getName(), globalCtx.getTitle());
                    bpmExecutionPlugin.execute(executionSession, globalCtx.getBpmPluginDef());
                } catch (Exception e2) {
                    this.LOG.error("【{}】节点执行全局插件【{}】出现异常:{}", new Object[]{bpmNodeDef.getName(), globalCtx.getTitle(), e2.getMessage(), e2});
                    throw new BusinessException(globalCtx.getTitle(), BpmStatusCode.PLUGIN_ERROR, e2);
                }
            }
        }
        this.LOG.debug("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑【{}】插件执行完毕↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑", eventType.getValue());
    }
}
