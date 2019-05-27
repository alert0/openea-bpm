package org.openbpm.bpm.engine.action.handler.instance;

import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.constant.NodeType;
import org.openbpm.bpm.api.engine.action.cmd.ActionCmd;
import org.openbpm.bpm.api.engine.action.handler.ActionHandler;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.springframework.stereotype.Component;

@Component
public class InstanceTaskOpinionActionHandler implements ActionHandler {
    public void execute(ActionCmd model) {
    }

    public ActionType getActionType() {
        return ActionType.TASKOPINION;
    }

    public int getSn() {
        return 5;
    }

    public Boolean isSupport(BpmNodeDef nodeDef) {
        if (nodeDef.getType() == NodeType.START) {
            return Boolean.valueOf(false);
        }
        return Boolean.valueOf(true);
    }

    public Boolean isDefault() {
        return Boolean.valueOf(true);
    }

    public String getConfigPage() {
        return "/bpm/instance/taskOpinionHistoryDialog.html";
    }

    public String getDefaultGroovyScript() {
        return "";
    }

    public String getDefaultBeforeScript() {
        return "";
    }
}
