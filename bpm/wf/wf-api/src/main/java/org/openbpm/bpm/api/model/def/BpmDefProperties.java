package org.openbpm.bpm.api.model.def;

import org.openbpm.bpm.api.model.def.IBpmDefinition.STATUS;
import java.io.Serializable;
import org.hibernate.validator.constraints.NotBlank;

public class BpmDefProperties implements Serializable {
    protected boolean allowExecutorEmpty = true;
    protected String description = "";
    protected boolean logSubmitData = true;
    @NotBlank
    protected String status = STATUS.DRAFT;
    @NotBlank
    protected String subjectRule = "{发起人:startorName}在{发起时间:startDate}发起{流程标题:title}";
    protected Integer supportMobile = Integer.valueOf(0);

    public String getSubjectRule() {
        return this.subjectRule;
    }

    public void setSubjectRule(String subjectRule2) {
        this.subjectRule = subjectRule2;
    }

    public String getDescription() {
        if (this.description == null) {
            return "";
        }
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public boolean isAllowExecutorEmpty() {
        return this.allowExecutorEmpty;
    }

    public void setAllowExecutorEmpty(boolean allowExecutorEmpty2) {
        this.allowExecutorEmpty = allowExecutorEmpty2;
    }

    public void setSupportMobile(Integer supportMobile2) {
        this.supportMobile = supportMobile2;
    }

    public Integer getSupportMobile() {
        return this.supportMobile;
    }

    public String getStatus() {
        return this.status;
    }

    public boolean isLogSubmitData() {
        return this.logSubmitData;
    }

    public void setLogSubmitData(boolean logSubmitData2) {
        this.logSubmitData = logSubmitData2;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }
}
