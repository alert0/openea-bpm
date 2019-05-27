package org.openbpm.bpm.api.engine.action.handler;

import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.engine.action.cmd.ActionCmd;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;

public interface ActionHandler<T extends ActionCmd> {
    void execute(T t);

    ActionType getActionType();

    String getConfigPage();

    String getDefaultBeforeScript();

    String getDefaultGroovyScript();

    int getSn();

    Boolean isDefault();

    Boolean isSupport(BpmNodeDef bpmNodeDef);
}
