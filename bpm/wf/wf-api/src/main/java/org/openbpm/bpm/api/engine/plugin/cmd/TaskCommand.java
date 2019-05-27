package org.openbpm.bpm.api.engine.plugin.cmd;

import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.engine.action.cmd.TaskActionCmd;

public interface TaskCommand {
    void execute(EventType eventType, TaskActionCmd taskActionCmd);
}
