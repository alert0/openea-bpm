package org.openbpm.bpm.engine.listener;

import org.openbpm.base.core.id.IdUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.act.listener.ActEventListener;
import org.openbpm.bpm.api.engine.action.cmd.BaseActionCmd;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.model.inst.BpmExecutionStack;
import org.openbpm.bpm.core.manager.BpmTaskStackManager;
import org.openbpm.bpm.core.model.BpmTaskStack;
import java.util.Date;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.impl.ActivitiSequenceFlowTakenEventImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SequenceFlowTakenListener implements ActEventListener {
    @Autowired
    private BpmTaskStackManager bpmTaskStackManager;

    public void notify(ActivitiEvent event) {
        ActivitiSequenceFlowTakenEventImpl sequenceFlowEvent = (ActivitiSequenceFlowTakenEventImpl) event;
        BaseActionCmd actionCmd = (BaseActionCmd) BpmContext.getActionModel();
        BpmExecutionStack oldTaskStack = actionCmd.getExecutionStack();
        BpmTaskStack sequenceFlowStack = new BpmTaskStack();
        sequenceFlowStack.setId(IdUtil.getSuid());
        sequenceFlowStack.setNodeId(sequenceFlowEvent.getId());
        if (StringUtil.isEmpty(sequenceFlowStack.getNodeId())) {
            sequenceFlowStack.setNodeId("back");
        }
        sequenceFlowStack.setNodeName(String.format("%s-》%s", new Object[]{sequenceFlowEvent.getSourceActivityId(), sequenceFlowEvent.getTargetActivityId()}));
        sequenceFlowStack.setTaskId(event.getExecutionId());
        sequenceFlowStack.setStartTime(new Date());
        sequenceFlowStack.setEndTime(new Date());
        sequenceFlowStack.setInstId(actionCmd.getInstanceId());
        sequenceFlowStack.setNodeType("sequenceFlow");
        sequenceFlowStack.setActionName(BpmContext.getActionModel());
        if (oldTaskStack == null) {
            sequenceFlowStack.setParentId("0");
        } else {
            sequenceFlowStack.setParentId(oldTaskStack.getId());
        }
        this.bpmTaskStackManager.create(sequenceFlowStack);
        actionCmd.setExecutionStack(sequenceFlowStack);
    }
}
