package org.openbpm.bpm.engine.action.handler.task;

import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.springframework.stereotype.Component;

@Component
public class TaskSaveActionHandler extends AbstractTaskActionHandler<DefualtTaskActionCmd> {
    public ActionType getActionType() {
        return ActionType.SAVE;
    }

    public void doAction(DefualtTaskActionCmd actionModel) {
    }

    /* access modifiers changed from: protected */
    public void doActionAfter(DefualtTaskActionCmd actionModel) {
    }

    /* access modifiers changed from: protected */
    public void doActionBefore(DefualtTaskActionCmd actionModel) {
    }

    public int getSn() {
        return 1;
    }

    public String getConfigPage() {
        return "";
    }

    public Boolean isDefault() {
        return Boolean.valueOf(true);
    }
}
