package org.openbpm.bpm.api.model.nodedef.impl;

import org.openbpm.bpm.api.constant.NodeType;
import org.openbpm.bpm.api.model.def.BpmProcessDef;

public class SubProcessNodeDef extends BaseBpmNodeDef {
    private static final long serialVersionUID = -1165886168391484970L;
    private BpmProcessDef bpmChildProcessDef;

    public SubProcessNodeDef() {
        setType(NodeType.SUBPROCESS);
    }

    public BpmProcessDef getChildBpmProcessDef() {
        return this.bpmChildProcessDef;
    }

    public void setChildBpmProcessDef(BpmProcessDef bpmChildProcessDef2) {
        this.bpmChildProcessDef = bpmChildProcessDef2;
    }
}
