package org.openbpm.bpm.engine.parser.node;

import org.openbpm.bpm.api.model.form.DefaultForm;
import org.openbpm.bpm.api.model.nodedef.impl.BaseBpmNodeDef;
import org.openbpm.bpm.engine.parser.FlowConfigConstants.NODE;
import org.springframework.stereotype.Component;

@Component
public class PcFormParse extends AbsNodeParse<DefaultForm> {
    public String getKey() {
        return NODE.PC_FORM;
    }

    public void setDefParam(BaseBpmNodeDef userNodeDef, Object object) {
        userNodeDef.setForm((DefaultForm) object);
    }
}
