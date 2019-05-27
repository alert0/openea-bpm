package org.openbpm.bpm.engine.plugin.session;

import org.openbpm.bpm.api.model.task.IBpmTask;

public interface BpmUserCalcPluginSession extends BpmPluginSession {
    IBpmTask getBpmTask();

    Boolean isPreVrewModel();
}
