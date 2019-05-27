package org.openbpm.bpm.engine.plugin.cmd;

import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.engine.action.cmd.InstanceActionCmd;
import org.openbpm.bpm.api.engine.plugin.cmd.ExecutionCommand;
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
import org.springframework.stereotype.Component;

@Component
public class ExecutionPluginCommand implements ExecutionCommand {
    protected Logger LOG = LoggerFactory.getLogger(getClass());
    @Resource
    BpmProcessDefService bpmProcessDefService;

    public void execute(EventType eventType, InstanceActionCmd actionModel) {
        this.LOG.debug("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓【{}】插件执行开始↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓", eventType.getValue());
        String defId = actionModel.getDefId();
        DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(defId);
        this.LOG.trace("============【全局插件】========{}=========", Integer.valueOf(bpmProcessDef.getBpmPluginContexts().size()));
        for (BpmPluginContext bpmPluginContext : bpmProcessDef.getBpmPluginContexts()) {
            executeContext(bpmPluginContext, actionModel, eventType);
        }
        BpmNodeDef nodeDef = null;
        if (eventType == EventType.START_POST_EVENT || eventType == EventType.START_EVENT) {
            nodeDef = this.bpmProcessDefService.getStartEvent(defId);
        } else if (eventType == EventType.END_EVENT || eventType == EventType.END_POST_EVENT || eventType == EventType.MANUAL_END) {
            nodeDef = (BpmNodeDef) this.bpmProcessDefService.getEndEvents(defId).get(0);
        }
        if (!(nodeDef == null || nodeDef.getBpmPluginContexts() == null)) {
            this.LOG.trace("============【{}插件】========{}=========", eventType.getValue(), Integer.valueOf(bpmProcessDef.getBpmPluginContexts().size()));
            for (BpmPluginContext bpmPluginContext2 : nodeDef.getBpmPluginContexts()) {
                executeContext(bpmPluginContext2, actionModel, eventType);
            }
        }
        this.LOG.debug("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑【{}】插件执行完毕↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑", eventType.getValue());
    }

    private void executeContext(BpmPluginContext bpmPluginContext, InstanceActionCmd actionModel, EventType eventType) {
        BpmPluginDef bpmPluginDef = bpmPluginContext.getBpmPluginDef();
        if (bpmPluginDef instanceof BpmExecutionPluginDef) {
            BpmExecutionPluginDef bpmExecutionPluginDef = (BpmExecutionPluginDef) bpmPluginDef;
            BpmExecutionPlugin<BpmExecutionPluginSession, BpmExecutionPluginDef> bpmExecutionPlugin = BpmPluginFactory.buildExecutionPlugin(bpmPluginContext, eventType);
            BpmExecutionPluginSession session = BpmPluginSessionFactory.buildExecutionPluginSession(actionModel, eventType);
            if (bpmExecutionPlugin != null) {
                try {
                    bpmExecutionPlugin.execute(session, bpmExecutionPluginDef);
                    this.LOG.debug("==>执行插件【{}】", bpmPluginContext.getTitle());
                } catch (Exception e) {
                    this.LOG.error("执行插件【{}】出现异常:{}", new Object[]{bpmPluginContext.getTitle(), e.getMessage(), e});
                    throw new BusinessException(bpmPluginContext.getTitle(), BpmStatusCode.PLUGIN_ERROR, e);
                }
            }
        }
    }
}
