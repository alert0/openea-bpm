package org.openbpm.bpm.engine.action.handler.task;

import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.model.def.NodeProperties;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TaskReject2StartActionHandler extends TaskRejectActionHandler {
    protected String getPreDestination(DefualtTaskActionCmd actionModel, NodeProperties nodeProperties) {
        List<BpmNodeDef> nodeDefs = this.bpmProcessDefService.getStartNodes(actionModel.getDefId());
        if (nodeDefs.size() > 1) {
        }
        return ((BpmNodeDef) nodeDefs.get(0)).getNodeId();
    }

    public ActionType getActionType() {
        return ActionType.REJECT2START;
    }

    public int getSn() {
        return 4;
    }

    public Boolean isDefault() {
        return Boolean.valueOf(false);
    }

    public String getConfigPage() {
        return "/bpm/task/taskOpinionDialog.html";
    }
}
