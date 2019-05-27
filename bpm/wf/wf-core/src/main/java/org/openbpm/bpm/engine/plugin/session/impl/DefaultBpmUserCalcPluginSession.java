package org.openbpm.bpm.engine.plugin.session.impl;

import org.openbpm.bpm.api.model.task.IBpmTask;
import org.openbpm.bpm.engine.plugin.session.BpmUserCalcPluginSession;

public class DefaultBpmUserCalcPluginSession extends DefaultBpmExecutionPluginSession implements BpmUserCalcPluginSession {
    private static final long serialVersionUID = 1132300282829841447L;
    private IBpmTask bpmTask;
    private Boolean isPreVrewModel = Boolean.valueOf(false);

    public IBpmTask getBpmTask() {
        return this.bpmTask;
    }

    public void setBpmTask(IBpmTask bpmTask2) {
        put("bpmTask", bpmTask2);
        this.bpmTask = bpmTask2;
    }

    public Boolean isPreVrewModel() {
        return this.isPreVrewModel;
    }

    public void setIsPreVrewModel(Boolean isPreVrewModel2) {
        this.isPreVrewModel = isPreVrewModel2;
    }
}
