package org.openbpm.bpm.engine.plugin.session;

import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.model.inst.IBpmInstance;
import org.openbpm.bus.api.model.IBusinessData;
import java.util.Map;
import org.activiti.engine.delegate.VariableScope;

public interface BpmPluginSession extends Map<String, Object> {
    Map<String, IBusinessData> getBoDatas();

    IBpmInstance getBpmInstance();

    EventType getEventType();

    VariableScope getVariableScope();
}
