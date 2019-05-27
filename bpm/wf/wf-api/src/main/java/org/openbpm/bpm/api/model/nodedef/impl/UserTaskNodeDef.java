package org.openbpm.bpm.api.model.nodedef.impl;

import org.openbpm.bpm.api.model.def.BpmVariableDef;
import java.util.List;

public class UserTaskNodeDef extends BaseBpmNodeDef {
    private List<BpmVariableDef> variableList;

    public List<BpmVariableDef> getVariableList() {
        return this.variableList;
    }

    public void setVariableList(List<BpmVariableDef> variableList2) {
        this.variableList = variableList2;
    }
}
