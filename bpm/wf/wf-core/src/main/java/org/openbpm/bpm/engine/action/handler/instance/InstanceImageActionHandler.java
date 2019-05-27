package org.openbpm.bpm.engine.action.handler.instance;

import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.engine.action.cmd.ActionCmd;
import org.openbpm.bpm.api.engine.action.handler.ActionHandler;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.springframework.stereotype.Component;

@Component
public class InstanceImageActionHandler implements ActionHandler {
    public void execute(ActionCmd model) {
    }

    public ActionType getActionType() {
        return ActionType.FLOWIMAGE;
    }

    public int getSn() {
        return 6;
    }

    public Boolean isSupport(BpmNodeDef nodeDef) {
        return Boolean.valueOf(true);
    }

    public Boolean isDefault() {
        return Boolean.valueOf(true);
    }

    public String getConfigPage() {
        return "/bpm/instance/instanceImageDialog.html";
    }

    public String getDefaultGroovyScript() {
        return "";
    }

    public String getDefaultBeforeScript() {
        return "";
    }
}
