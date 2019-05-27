package org.openbpm.bpm.api.engine.plugin.context;

import org.openbpm.bpm.api.engine.plugin.def.BpmPluginDef;
import org.openbpm.bpm.api.engine.plugin.runtime.RunTimePlugin;
import java.io.Serializable;

public interface PluginContext extends Serializable {
    public static final String PLUGINCONTEXT = "PluginContext";

    BpmPluginDef getBpmPluginDef();

    Class<? extends RunTimePlugin> getPluginClass();

    String getTitle();
}
