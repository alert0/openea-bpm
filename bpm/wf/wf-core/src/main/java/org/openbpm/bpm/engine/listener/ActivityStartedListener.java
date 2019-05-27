package org.openbpm.bpm.engine.listener;

import org.openbpm.bpm.act.listener.ActEventListener;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.core.manager.BpmTaskStackManager;
import org.openbpm.bpm.core.model.BpmTask;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import javax.annotation.Resource;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.impl.ActivitiActivityEventImpl;
import org.springframework.stereotype.Component;

@Component
public class ActivityStartedListener implements ActEventListener {
    @Resource
    BpmTaskStackManager bpmTaskStackMananger;

    public void notify(ActivitiEvent event) {
        if (event instanceof ActivitiActivityEventImpl) {
            ActivitiActivityEventImpl activitEvent = (ActivitiActivityEventImpl) event;
            if (activitEvent.getActivityType().equals("callActivity")) {
                createCallActvitiviStack(activitEvent);
            }
        }
    }

    private void createCallActvitiviStack(ActivitiActivityEventImpl activitEvent) {
        DefualtTaskActionCmd action = (DefualtTaskActionCmd) BpmContext.getActionModel();
        BpmTask task = new BpmTask();
        task.setId(activitEvent.getExecutionId());
        task.setName(activitEvent.getActivityName());
        task.setNodeId(activitEvent.getActivityId());
        task.setInstId(action.getInstanceId());
        this.bpmTaskStackMananger.createStackByTask(task, action.getExecutionStack());
    }
}
