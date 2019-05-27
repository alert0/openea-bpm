package org.openbpm.bpm.engine.listener;

import org.openbpm.base.core.id.IdUtil;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.constant.InstanceStatus;
import org.openbpm.bpm.api.constant.ScriptType;
import org.openbpm.bpm.api.engine.action.cmd.BaseActionCmd;
import org.openbpm.bpm.api.engine.action.cmd.InstanceActionCmd;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.model.inst.BpmExecutionStack;
import org.openbpm.bpm.core.manager.BpmInstanceManager;
import org.openbpm.bpm.core.manager.BpmTaskOpinionManager;
import org.openbpm.bpm.core.manager.BpmTaskStackManager;
import org.openbpm.bpm.core.model.BpmInstance;
import org.openbpm.bpm.core.model.BpmTaskStack;
import org.openbpm.bpm.engine.action.cmd.DefaultInstanceActionCmd;
import java.util.Date;
import javax.annotation.Resource;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.stereotype.Component;

@Component
public class InstanceEndEventListener extends AbstractInstanceListener {
    @Resource
    BpmInstanceManager bpmInstanceManager;
    @Resource
    BpmTaskOpinionManager bpmTaskOpinionManager;
    @Resource
    BpmTaskStackManager bpmTaskStackManager;

    public EventType getBeforeTriggerEventType() {
        return EventType.END_EVENT;
    }

    public EventType getAfterTriggerEventType() {
        return EventType.END_POST_EVENT;
    }

    public void beforePluginExecute(InstanceActionCmd instanceActionModel) {
        this.LOG.debug("流程实例【{}】,ID【{}】开始触发终止事件", instanceActionModel.getBpmInstance().getSubject(), instanceActionModel.getBpmInstance().getId());
    }

    public void triggerExecute(InstanceActionCmd instanceActionModel) {
        this.bpmTaskOpinionManager.createOpinionByInstance(instanceActionModel, false);
        BpmInstance instance = (BpmInstance) instanceActionModel.getBpmInstance();
        instance.setStatus(InstanceStatus.STATUS_END.getKey());
        instance.setEndTime(new Date());
        instance.setDuration(Long.valueOf(instance.getEndTime().getTime() - instance.getCreateTime().getTime()));
        this.bpmInstanceManager.update(instance);
        createExecutionStack(instanceActionModel);
    }

    public void afterPluginExecute(InstanceActionCmd instanceActionModel) {
        this.LOG.debug("流程实例【{}】,ID【{}】已经终止", instanceActionModel.getBpmInstance().getSubject(), instanceActionModel.getBpmInstance().getId());
    }

    protected ScriptType getScriptType() {
        return ScriptType.END;
    }

    protected InstanceActionCmd getInstanceActionModel(ExecutionEntity excutionEntity) {
        BaseActionCmd actionModle = (BaseActionCmd) BpmContext.getActionModel();
        DefaultInstanceActionCmd instanceModel = new DefaultInstanceActionCmd();
        instanceModel.setBpmInstance(actionModle.getBpmInstance());
        instanceModel.setBpmDefinition(actionModle.getBpmDefinition());
        instanceModel.setBizDataMap(actionModle.getBizDataMap());
        instanceModel.setBusinessKey(actionModle.getBusinessKey());
        instanceModel.setActionName("end");
        instanceModel.setExecutionEntity(excutionEntity);
        return instanceModel;
    }

    private void createExecutionStack(InstanceActionCmd instanceActionModel) {
        DefaultInstanceActionCmd actionCmd = (DefaultInstanceActionCmd) instanceActionModel;
        ExecutionEntity entity = actionCmd.getExecutionEntity();
        BpmTaskStack endFlowStack = new BpmTaskStack();
        endFlowStack.setId(IdUtil.getSuid());
        endFlowStack.setNodeId(entity.getActivityId());
        endFlowStack.setNodeName(entity.getCurrentActivityName());
        endFlowStack.setTaskId("0");
        endFlowStack.setStartTime(new Date());
        endFlowStack.setEndTime(new Date());
        endFlowStack.setInstId(actionCmd.getInstanceId());
        endFlowStack.setNodeType("endNoneEvent");
        endFlowStack.setActionName("end");
        BpmExecutionStack parentStack = actionCmd.getExecutionStack();
        if (parentStack != null) {
            endFlowStack.setParentId(parentStack.getId());
        }
        this.bpmTaskStackManager.create(endFlowStack);
    }
}
