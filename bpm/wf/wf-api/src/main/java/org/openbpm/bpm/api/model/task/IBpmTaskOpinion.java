package org.openbpm.bpm.api.model.task;

import java.util.Date;

public interface IBpmTaskOpinion {
    Date getApproveTime();

    String getApprover();

    String getApproverName();

    String getAssignInfo();

    String getCreateBy();

    Date getCreateTime();

    String getFormId();

    String getId();

    String getInstId();

    String getOpinion();

    String getStatus();

    String getSupInstId();

    String getTaskId();

    String getTaskKey();

    String getTaskName();

    String getToken();
}
