package org.openbpm.bpm.api.model.inst;

import java.util.Date;

public interface BpmExecutionStack {
    String getActionName();

    Date getEndTime();

    String getId();

    String getInstId();

    Short getIsMulitiTask();

    String getNodeId();

    String getNodeName();

    String getNodeType();

    String getParentId();

    Date getStartTime();

    String getTaskId();

    void setActionName(String str);

    void setEndTime(Date date);

    void setId(String str);

    void setInstId(String str);

    void setIsMulitiTask(Short sh);

    void setNodeId(String str);

    void setNodeName(String str);

    void setNodeType(String str);

    void setParentId(String str);

    void setStartTime(Date date);

    void setTaskId(String str);
}
