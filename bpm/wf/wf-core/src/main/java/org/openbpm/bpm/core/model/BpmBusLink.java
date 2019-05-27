package org.openbpm.bpm.core.model;

import lombok.Data;
import org.openbpm.base.api.model.IDModel;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
public class BpmBusLink implements IDModel {

    protected String bizCode;
    protected String bizId;
    protected String defId;
    protected String id;
    protected String instId;

    public String toString() {
        return new ToStringBuilder(this).append("id", this.id).append("defId", this.defId).append("instId", this.instId).append("bizKey", this.bizId).append("bizIdentify", this.bizCode).toString();
    }

}
