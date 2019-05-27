package org.openbpm.bpm.api.engine.plugin.context;

import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.engine.plugin.def.BpmPluginDef;
import java.util.List;

public interface BpmPluginContext<T extends BpmPluginDef> extends PluginContext, PluginParse<T>, Comparable<BpmPluginContext> {
    List<EventType> getEventTypes();

    Integer getSn();
}
