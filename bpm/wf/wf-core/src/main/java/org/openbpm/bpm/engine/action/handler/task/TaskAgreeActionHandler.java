package org.openbpm.bpm.engine.action.handler.task;

import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.springframework.stereotype.Component;

@Component
public class TaskAgreeActionHandler extends AbstractTaskActionHandler<DefualtTaskActionCmd> {
    public ActionType getActionType() {
        return ActionType.AGREE;
    }

    /* access modifiers changed from: protected */
    public void doActionAfter(DefualtTaskActionCmd actionModel) {
    }

    /* access modifiers changed from: protected */
    public void doActionBefore(DefualtTaskActionCmd actionModel) {
        taskComplatePrePluginExecute(actionModel);
    }

    public int getSn() {
        return 1;
    }

    public String getConfigPage() {
        return "/bpm/task/taskOpinionDialog.html";
    }
}
