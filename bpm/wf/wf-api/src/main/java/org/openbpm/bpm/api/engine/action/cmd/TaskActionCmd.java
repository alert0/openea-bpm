package org.openbpm.bpm.api.engine.action.cmd;

import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.model.task.IBpmTask;

public interface TaskActionCmd extends ActionCmd {
    ActionType getActionType();

    IBpmTask getBpmTask();

    String getDestination();

    String getNodeId();

    String getOpinion();

    String getTaskId();

    void setBpmTask(IBpmTask iBpmTask);

    void setDestination(String str);
}
