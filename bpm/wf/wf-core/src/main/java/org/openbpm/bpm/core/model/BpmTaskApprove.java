package org.openbpm.bpm.core.model;

import lombok.Data;

import java.util.Date;

@Data
public class BpmTaskApprove {

    protected String actInstId;
    protected String approveStatus;
    protected Date approveTime;
    protected String createBy;
    protected Date createTime;
    protected String creator;
    protected String defName;
    protected Long durMs;
    protected Long duration;
    protected Date endTime;
    protected String id;
    protected String nodeId;
    protected String nodeName;
    protected String status;
    protected String subject;
    protected String typeId;

}
