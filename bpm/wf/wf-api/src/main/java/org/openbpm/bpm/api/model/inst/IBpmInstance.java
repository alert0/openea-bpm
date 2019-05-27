package org.openbpm.bpm.api.model.inst;

import java.util.Date;

public interface IBpmInstance {
    public static final Short INSTANCE_FORBIDDEN = 1;
    public static final Short INSTANCE_NO_FORBIDDEN = 0;

    String getActDefId();

    String getActInstId();

    String getBizKey();

    String getCreateBy();

    String getCreateOrgId();

    Date getCreateTime();

    String getCreator();

    String getDataMode();

    String getDefId();

    String getDefKey();

    String getDefName();

    Long getDuration();

    Date getEndTime();

    String getId();

    Short getIsForbidden();

    String getIsFormmal();

    String getParentInstId();

    String getStatus();

    String getSubject();

    String getSuperNodeId();

    Integer getSupportMobile();

    String getTypeId();

    String getUpdateBy();

    Date getUpdateTime();

    Boolean hasCreate();

    void setHasCreate(Boolean bool);

    void setStatus(String key);
}
