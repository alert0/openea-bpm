package org.openbpm.bpm.core.model;

import lombok.Data;
import org.openbpm.base.api.model.IDModel;
import org.openbpm.bpm.api.engine.action.cmd.ActionCmd;
import org.openbpm.bpm.api.model.inst.BpmExecutionStack;
import java.util.Date;

@Data
public class BpmTaskStack implements IDModel, BpmExecutionStack {
    protected String actionName;
    protected Date endTime;
    protected String id;
    protected String instId;
    protected Short isMulitiTask;
    protected String nodeId;
    protected String nodeName;
    protected String nodeType;
    protected String parentId;
    protected Date startTime;
    protected String taskId;



    public void setActionName(ActionCmd actionModel) {
        if (actionModel != null) {
            setActionName(actionModel.getActionName());
        }
    }

    @Override
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
