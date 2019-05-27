package org.openbpm.bpm.api.model.nodedef.impl;

import org.openbpm.bpm.api.engine.plugin.context.BpmPluginContext;
import org.openbpm.bpm.api.model.def.BpmProcessDef;
import java.util.List;

public class GateWayBpmNodeDef extends BaseBpmNodeDef {
    public List<BpmPluginContext> getBpmPluginContexts() {
        throw new RuntimeException("GateWayBpmNodeDef not support getBpmPluginContexts method");
    }

    public BpmProcessDef getChildBpmProcessDef() {
        throw new RuntimeException("GateWayBpmNodeDef not support getChildBpmProcessDef method");
    }
}
