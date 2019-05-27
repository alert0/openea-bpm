package org.openbpm.bpm.engine.parser.flow;

import org.openbpm.bpm.api.model.def.BpmVariableDef;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import org.openbpm.bpm.engine.model.DefaultBpmVariableDef;
import org.openbpm.bpm.engine.parser.FlowConfigConstants.GLOBAL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class FlowVariablesParse extends AbsFlowParse<DefaultBpmVariableDef> {
    public String getKey() {
        return GLOBAL.VAR_LIST;
    }

    public String validate(Object object) {
        List<BpmVariableDef> varList = (List) object;
        Set<String> keys = new HashSet<>();
        for (BpmVariableDef def : varList) {
            String key = def.getKey();
            if (keys.contains(key)) {
                throw new RuntimeException("解析流程变量出错：" + key + "在流程中重复！");
            }
            keys.add(def.getKey());
        }
        return "";
    }

    public void setDefParam(DefaultBpmProcessDef bpmProcessDef, Object object) {
        bpmProcessDef.setVarList((List) object);
    }

    public boolean isArray() {
        return true;
    }
}
