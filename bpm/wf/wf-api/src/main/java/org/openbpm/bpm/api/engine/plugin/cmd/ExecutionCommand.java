package org.openbpm.bpm.api.engine.plugin.cmd;

import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.engine.action.cmd.InstanceActionCmd;

public interface ExecutionCommand {
    void execute(EventType eventType, InstanceActionCmd instanceActionCmd);
}
