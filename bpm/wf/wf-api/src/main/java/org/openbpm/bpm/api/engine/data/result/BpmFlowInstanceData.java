package org.openbpm.bpm.api.engine.data.result;

import org.openbpm.bpm.api.model.inst.IBpmInstance;
import io.swagger.annotations.ApiModelProperty;

public class BpmFlowInstanceData extends BpmFlowData {
    @ApiModelProperty("流程实例信息")
    private IBpmInstance instance;

    public IBpmInstance getInstance() {
        return this.instance;
    }

    public void setInstance(IBpmInstance instance2) {
        this.instance = instance2;
        this.defId = instance2.getDefId();
    }
}
