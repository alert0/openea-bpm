package org.openbpm.bpm.engine.parser.flow;

import org.openbpm.bpm.api.model.def.NodeInit;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import org.openbpm.bpm.engine.parser.FlowConfigConstants.GLOBAL;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FlowFormInitParse extends AbsFlowParse<NodeInit> {
    public String getKey() {
        return GLOBAL.NODE_INIT_LIST;
    }

    public void setDefParam(DefaultBpmProcessDef bpmProcessDef, Object object) {
        bpmProcessDef.setNodeInitList((List) object);
    }

    public boolean isArray() {
        return true;
    }
}
