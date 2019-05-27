package org.openbpm.bpm.core.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.openbpm.base.api.model.IBaseModel;
import org.openbpm.bpm.api.model.def.IBpmDefinition;
import org.openbpm.bpm.engine.parser.FlowConfigConstants.FORM;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "BpmDefinition")
public class BpmDefinition implements IBaseModel, IBpmDefinition {

    protected String actDefId;
    protected String actDeployId;
    protected String actModelId;
    protected String createBy;
    protected String createOrgId;
    protected Date createTime;
    @JSONField(serialize = false)
    @XmlElement(name = "defSetting")
    protected String defSetting;
    @XmlAttribute(name = "desc")
    protected String desc;
    protected String id;
    protected String isMain;
    @XmlAttribute(name = "key")
    protected String key;
    protected String mainDefId;
    @XmlAttribute(name = "name")
    protected String name;
    protected Integer rev = 0;
    @XmlAttribute(name = "status")
    protected String status = "draft";
    @XmlAttribute(name = "supportMobile")
    protected Integer supportMobile = Integer.valueOf(0);
    @XmlAttribute(name = "typeId")
    protected String typeId;
    protected String updateBy;
    protected Date updateTime;
    protected Integer version;

    public String toString() {
        return new ToStringBuilder(this).append("id", this.id).append(FORM.NAME, this.name).append("key", this.key).append("desc", this.desc).append("typeId", this.typeId).append("status", this.status).append("actDefId", this.actDefId).append("actModelId", this.actModelId).append("actDeployId", this.actDeployId).append("version", this.version).append("mainDefId", this.mainDefId).append("isMain", this.isMain).append("createBy", this.createBy).append("createTime", this.createTime).append("createOrgId", this.createOrgId).append("updateBy", this.updateBy).append("updateTime", this.updateTime).append("supportMobile", this.supportMobile).append("defSetting", this.defSetting).append("rev", this.rev).toString();
    }
}
