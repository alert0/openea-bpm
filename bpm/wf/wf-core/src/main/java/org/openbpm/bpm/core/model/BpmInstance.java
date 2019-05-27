package org.openbpm.bpm.core.model;

import lombok.Data;
import org.openbpm.base.api.model.IBaseModel;
import org.openbpm.bpm.api.model.inst.IBpmInstance;
import java.util.Date;

@Data
public class BpmInstance implements IBaseModel, IBpmInstance {
    protected String actDefId;
    protected String actInstId;
    protected String bizKey;
    protected String createBy;
    protected String createOrgId;
    protected Date createTime;
    protected String creator;
    protected String dataMode;
    protected String defId;
    protected String defKey;
    protected String defName;
    protected Long duration;
    protected Date endTime;
    protected transient boolean hasCreate = true;
    protected String id;
    protected Short isForbidden = 0;
    protected String isFormmal;
    protected String parentInstId;
    protected String status;
    protected String subject;
    protected String superNodeId;
    protected Integer supportMobile;
    protected String typeId;
    protected String updateBy;
    protected Date updateTime;


    public String getBizKey() {
        if (this.bizKey == null) {
            return "";
        }
        return this.bizKey;
    }

    public Boolean hasCreate() {
        return Boolean.valueOf(this.hasCreate);
    }

    public void setHasCreate(Boolean hasCreate2) {
        this.hasCreate = hasCreate2.booleanValue();
    }
}
