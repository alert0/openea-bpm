package org.openbpm.bpm.api.engine.plugin.def;

import org.openbpm.bpm.api.constant.ExtractType;
import org.openbpm.bpm.api.engine.constant.LogicType;

public interface BpmUserCalcPluginDef extends BpmPluginDef {
    ExtractType getExtract();

    LogicType getLogicCal();

    void setExtract(ExtractType extractType);

    void setLogicCal(LogicType logicType);
}
