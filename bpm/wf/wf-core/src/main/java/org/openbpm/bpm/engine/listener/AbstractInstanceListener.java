package org.openbpm.bpm.engine.listener;

import org.openbpm.bpm.act.listener.ActEventListener;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.constant.ScriptType;
import org.openbpm.bpm.api.engine.action.cmd.InstanceActionCmd;
import org.openbpm.bpm.api.engine.plugin.cmd.ExecutionCommand;
import javax.annotation.Resource;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractInstanceListener implements ActEventListener {
    protected Logger LOG = LoggerFactory.getLogger(getClass());
    @Resource
    private ExecutionCommand executionCommand;

    public abstract void afterPluginExecute(InstanceActionCmd instanceActionCmd);

    public abstract void beforePluginExecute(InstanceActionCmd instanceActionCmd);

    public abstract EventType getAfterTriggerEventType();

    public abstract EventType getBeforeTriggerEventType();

    protected abstract InstanceActionCmd getInstanceActionModel(ExecutionEntity executionEntity);

    protected abstract ScriptType getScriptType();

    public abstract void triggerExecute(InstanceActionCmd instanceActionCmd);

    public void notify(ActivitiEvent event) {
        InstanceActionCmd actionModel = getInstanceActionModel((ExecutionEntity) ((ActivitiEntityEventImpl) event).getEntity());
        beforePluginExecute(actionModel);
        if (getBeforeTriggerEventType() != null) {
            this.executionCommand.execute(getBeforeTriggerEventType(), actionModel);
        }
        triggerExecute(actionModel);
        if (getAfterTriggerEventType() != null) {
            this.executionCommand.execute(getAfterTriggerEventType(), actionModel);
        }
        afterPluginExecute(actionModel);
    }
}
