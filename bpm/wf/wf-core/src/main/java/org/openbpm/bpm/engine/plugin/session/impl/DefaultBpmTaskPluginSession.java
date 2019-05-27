package org.openbpm.bpm.engine.plugin.session.impl;

import org.openbpm.bpm.api.model.task.IBpmTask;

public class DefaultBpmTaskPluginSession extends DefaultBpmExecutionPluginSession {
    private static final long serialVersionUID = 1;
    private IBpmTask bpmTask;

    public IBpmTask getBpmTask() {
        return this.bpmTask;
    }

    public void setBpmTask(IBpmTask bpmTask2) {
        put("bpmTask", bpmTask2);
        this.bpmTask = bpmTask2;
    }
}
