package org.openbpm.bpm.engine.plugin.runtime.abstact;

import org.openbpm.bpm.api.engine.action.cmd.ActionCmd;
import org.openbpm.bpm.api.engine.action.cmd.TaskActionCmd;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.engine.plugin.def.BpmExecutionPluginDef;
import org.openbpm.bpm.api.model.task.IBpmTask;
import org.openbpm.bpm.engine.plugin.runtime.BpmExecutionPlugin;
import org.openbpm.bpm.engine.plugin.session.BpmExecutionPluginSession;
import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBpmExecutionPlugin<S extends BpmExecutionPluginSession, M extends BpmExecutionPluginDef> implements BpmExecutionPlugin<S, M> {
    protected Logger LOG = LoggerFactory.getLogger(getClass());

    protected boolean isTask() {
        if (BpmContext.getActionModel() instanceof TaskActionCmd) {
            return true;
        }
        return false;
    }

    protected IBpmTask getTaskIfTask() {
        ActionCmd actionCmd = BpmContext.getActionModel();
        if (actionCmd instanceof TaskActionCmd) {
            return ((TaskActionCmd) actionCmd).getBpmTask();
        }
        return null;
    }

    protected String getActivitiId(BpmExecutionPluginSession session) {
        VariableScope scope = session.getVariableScope();
        if (scope instanceof ExecutionEntity) {
            return ((ExecutionEntity) scope).getActivityId();
        }
        if (scope instanceof TaskEntity) {
            return ((TaskEntity) scope).getTaskDefinitionKey();
        }
        return null;
    }
}
