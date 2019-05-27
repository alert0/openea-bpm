package org.openbpm.bpm.api.model.task;

import java.util.Date;

public interface IBpmTask {
    public static final short STATUS_NO_SUSPEND = 0;
    public static final short STATUS_SUSPEND = 1;

    String getActExecutionIdId();

    String getActInstId();

    String getAssigneeId();

    Date getCreateTime();

    String getDefId();

    Date getDueTime();

    String getId();

    String getInstId();

    String getName();

    String getNodeId();

    String getParentId();

    Integer getPriority();

    String getStatus();

    String getSubject();

    String getTaskId();

    String getTaskType();

    void setAssigneeId(String str);

    void setAssigneeNames(String str);
}
