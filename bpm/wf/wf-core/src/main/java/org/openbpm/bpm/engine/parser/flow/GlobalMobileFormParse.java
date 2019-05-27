package org.openbpm.bpm.engine.parser.flow;

import org.openbpm.bpm.api.model.form.DefaultForm;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import org.openbpm.bpm.engine.parser.FlowConfigConstants.GLOBAL;
import org.springframework.stereotype.Component;

@Component
public class GlobalMobileFormParse extends AbsFlowParse<DefaultForm> {
    public String getKey() {
        return GLOBAL.MOBILE_FORM;
    }

    public void setDefParam(DefaultBpmProcessDef bpmProcessDef, Object object) {
        bpmProcessDef.setGlobalMobileForm((DefaultForm) object);
    }
}
