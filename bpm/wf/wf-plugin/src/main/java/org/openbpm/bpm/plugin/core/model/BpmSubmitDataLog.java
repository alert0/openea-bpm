package org.openbpm.bpm.plugin.core.model;

import org.openbpm.base.core.model.BaseModel;

public class BpmSubmitDataLog extends BaseModel {
    protected String data;
    protected String destination;
    protected String extendConf;
    protected String instId;
    protected String taskId;

    public void setTaskId(String taskId2) {
        this.taskId = taskId2;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setInstId(String instId2) {
        this.instId = instId2;
    }

    public String getInstId() {
        return this.instId;
    }

    public void setData(String data2) {
        this.data = data2;
    }

    public String getData() {
        return this.data;
    }

    public void setDestination(String destination2) {
        this.destination = destination2;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setExtendConf(String extendConf2) {
        this.extendConf = extendConf2;
    }

    public String getExtendConf() {
        return this.extendConf;
    }
}
