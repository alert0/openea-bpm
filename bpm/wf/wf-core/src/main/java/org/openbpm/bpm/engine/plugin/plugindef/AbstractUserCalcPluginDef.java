package org.openbpm.bpm.engine.plugin.plugindef;

import org.openbpm.bpm.api.constant.ExtractType;
import org.openbpm.bpm.api.engine.constant.LogicType;
import org.openbpm.bpm.api.engine.plugin.def.BpmUserCalcPluginDef;

public abstract class AbstractUserCalcPluginDef implements BpmUserCalcPluginDef {
    private ExtractType extractType = ExtractType.EXACT_NOEXACT;
    private LogicType logicType = LogicType.OR;

    public ExtractType getExtract() {
        return this.extractType;
    }

    public void setExtract(ExtractType type) {
        this.extractType = type;
    }

    public LogicType getLogicCal() {
        return this.logicType;
    }

    public void setLogicCal(LogicType logicType2) {
        this.logicType = logicType2;
    }
}
