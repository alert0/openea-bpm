package org.openbpm.bpm.engine.listener;

import org.openbpm.bpm.act.listener.ActEventListener;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.constant.ScriptType;
import org.openbpm.bpm.api.engine.action.cmd.TaskActionCmd;
import org.openbpm.bpm.api.engine.plugin.cmd.TaskCommand;
import javax.annotation.Resource;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTaskListener<T extends TaskActionCmd> implements ActEventListener {
    protected Logger LOG = LoggerFactory.getLogger(getClass());
    @Resource
    protected TaskCommand taskCommand;

    public abstract void afterPluginExecute(T t);

    public abstract void beforePluginExecute(T t);

    public abstract T getActionModel(TaskEntity taskEntity);

    public abstract EventType getAfterTriggerEventType();

    public abstract EventType getBeforeTriggerEventType();

    protected abstract ScriptType getScriptType();

    public abstract void triggerExecute(T t);

    public void notify(ActivitiEvent event) {
        T model = getActionModel((TaskEntity) ((ActivitiEntityEvent) event).getEntity());
        beforePluginExecute(model);
        if (getBeforeTriggerEventType() != null) {
            this.taskCommand.execute(getBeforeTriggerEventType(), model);
        }
        triggerExecute(model);
        if (getAfterTriggerEventType() != null) {
            this.taskCommand.execute(getAfterTriggerEventType(), model);
        }
        afterPluginExecute(model);
    }
}
