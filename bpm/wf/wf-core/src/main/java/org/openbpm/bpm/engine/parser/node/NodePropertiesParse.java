package org.openbpm.bpm.engine.parser.node;

import org.openbpm.bpm.api.model.def.NodeProperties;
import org.openbpm.bpm.api.model.nodedef.impl.BaseBpmNodeDef;
import org.openbpm.bpm.engine.parser.FlowConfigConstants.NODE;
import org.springframework.stereotype.Component;

@Component
public class NodePropertiesParse extends AbsNodeParse<NodeProperties> {
    public String getKey() {
        return NODE.PROPERTIES;
    }

    public void setDefParam(BaseBpmNodeDef userNodeDef, Object object) {
        userNodeDef.setNodeProperties((NodeProperties) object);
    }
}
