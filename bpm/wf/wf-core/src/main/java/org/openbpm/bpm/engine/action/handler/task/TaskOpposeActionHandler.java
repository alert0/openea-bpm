package org.openbpm.bpm.engine.action.handler.task;

import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.constant.NodeType;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.springframework.stereotype.Component;

@Component
public class TaskOpposeActionHandler extends AbstractTaskActionHandler<DefualtTaskActionCmd> {
    public ActionType getActionType() {
        return ActionType.OPPOSE;
    }

    /* access modifiers changed from: protected */
    public void doActionAfter(DefualtTaskActionCmd actionModel) {
    }

    /* access modifiers changed from: protected */
    public void doActionBefore(DefualtTaskActionCmd actionModel) {
    }

    public int getSn() {
        return 2;
    }

    public Boolean isSupport(BpmNodeDef nodeDef) {
        NodeType nodeType = nodeDef.getType();
        if (nodeType == NodeType.USERTASK || nodeType == NodeType.SIGNTASK) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    public String getConfigPage() {
        return "/bpm/task/taskOpinionDialog.html";
    }

    public Boolean isDefault() {
        return Boolean.valueOf(false);
    }
}
