package org.openbpm.bpm.api.engine.event;

import org.openbpm.bpm.api.model.def.IBpmDefinition;
import org.springframework.context.ApplicationEvent;

public class BpmDefinitionUpdateEvent extends ApplicationEvent {
    private static final long serialVersionUID = 550560932524738231L;

    public BpmDefinitionUpdateEvent(IBpmDefinition source) {
        super(source);
    }
}
