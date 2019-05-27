package org.openbpm.bpm.engine.action.handler.instance;

import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.engine.action.cmd.ActionCmd;
import org.openbpm.bpm.api.engine.action.handler.ActionHandler;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.springframework.stereotype.Component;

@Component
public class InstancePrintActionHandler implements ActionHandler {
    public void execute(ActionCmd model) {
    }

    public ActionType getActionType() {
        return ActionType.PRINT;
    }

    public int getSn() {
        return 7;
    }

    public Boolean isSupport(BpmNodeDef nodeDef) {
        return Boolean.valueOf(true);
    }

    public Boolean isDefault() {
        return Boolean.valueOf(true);
    }

    public String getConfigPage() {
        return "";
    }

    public String getDefaultGroovyScript() {
        return "";
    }

    public String getDefaultBeforeScript() {
        return "print(); return false;";
    }
}
