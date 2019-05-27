package org.openbpm.bpm.api.engine.plugin.context;

import org.openbpm.bpm.api.engine.plugin.runtime.RunTimePlugin;

public interface UserQueryPluginContext {
    Class<? extends RunTimePlugin> getUserQueryPluginClass();
}
