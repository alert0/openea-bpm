package org.openbpm.bpm.core.model;

import lombok.Data;
import org.openbpm.base.api.model.IBaseModel;
import org.openbpm.bpm.api.model.task.IBpmTaskOpinion;
import java.util.Date;

@Data
public class BpmTaskOpinion implements IBaseModel, IBpmTaskOpinion {
    protected Date approveTime;
    protected String approver;
    protected String approverName;
    private String assignInfo;
    protected String createBy;
    protected Date createTime;
    protected Long durMs;
    protected String formId;
    protected String id;
    protected String instId;
    protected String opinion;
    protected String status;
    protected String supInstId;
    protected String taskId;
    protected String taskKey;
    protected String taskName;
    protected String token;


    @Override
    public Date getUpdateTime() {
        return null;
    }

    @Override
    public void setUpdateTime(Date updateTime) {

    }

    @Override
    public String getUpdateBy() {
        return null;
    }

    @Override
    public void setUpdateBy(String updateBy) {

    }
}
