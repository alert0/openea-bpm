package org.openbpm.bpm.engine.plugin.runtime;

import org.openbpm.bpm.api.engine.plugin.def.BpmExecutionPluginDef;
import org.openbpm.bpm.api.engine.plugin.runtime.RunTimePlugin;
import org.openbpm.bpm.engine.plugin.session.BpmExecutionPluginSession;

public interface BpmExecutionPlugin<S extends BpmExecutionPluginSession, M extends BpmExecutionPluginDef> extends RunTimePlugin<S, M, Void> {
}
