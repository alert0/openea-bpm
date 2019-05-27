package org.openbpm.bpm.core.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskIdentityLink implements Serializable {
    public static final String RIGHT_TYPE_USER = "user";
    protected String id;
    protected String identity;
    protected String identityName;
    protected String instId;
    protected String permissionCode;
    protected String taskId;
    protected String type;

}
