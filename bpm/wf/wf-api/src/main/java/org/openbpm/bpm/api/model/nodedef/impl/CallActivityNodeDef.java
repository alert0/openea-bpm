package org.openbpm.bpm.api.model.nodedef.impl;

public class CallActivityNodeDef extends BaseBpmNodeDef {
    private static final long serialVersionUID = -7321180599360290218L;
    private String flowKey = "";
    private String flowName = "";

    public String getFlowKey() {
        return this.flowKey;
    }

    public void setFlowKey(String flowKey2) {
        this.flowKey = flowKey2;
    }

    public String getFlowName() {
        return this.flowName;
    }

    public void setFlowName(String flowName2) {
        this.flowName = flowName2;
    }
}
