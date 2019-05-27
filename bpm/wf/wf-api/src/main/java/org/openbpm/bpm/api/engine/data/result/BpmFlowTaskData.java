package org.openbpm.bpm.api.engine.data.result;

import org.openbpm.bpm.api.model.task.IBpmTask;
import io.swagger.annotations.ApiModelProperty;

public class BpmFlowTaskData extends BpmFlowData {
    @ApiModelProperty("流程任务信息")
    private IBpmTask task;

    public IBpmTask getTask() {
        return this.task;
    }

    public void setTask(IBpmTask task2) {
        this.task = task2;
        this.defId = task2.getDefId();
    }
}
