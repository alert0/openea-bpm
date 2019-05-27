package org.openbpm.bpm.engine.plugin.runtime;

import org.openbpm.bpm.api.engine.plugin.def.BpmPluginDef;
import org.openbpm.bpm.api.engine.plugin.runtime.RunTimePlugin;
import org.openbpm.bpm.engine.plugin.session.BpmUserCalcPluginSession;
import org.openbpm.sys.api.model.SysIdentity;
import java.util.List;

public interface BpmUserCalcPlugin<M extends BpmPluginDef> extends RunTimePlugin<BpmUserCalcPluginSession, M, List<SysIdentity>> {
}
