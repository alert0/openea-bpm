package org.openbpm.bpm.core.model;

import lombok.Data;
import org.openbpm.base.api.model.IBaseModel;

import java.util.Date;

@Data
public class BpmTaskCandidate implements IBaseModel {
    protected String executor;
    protected String id;
    protected String instId;
    protected String taskId;
    protected String type;


    @Override
    public Date getCreateTime() {
        return null;
    }

    @Override
    public void setCreateTime(Date createTime) {

    }

    @Override
    public String getCreateBy() {
        return null;
    }

    @Override
    public void setCreateBy(String createBy) {

    }

    @Override
    public Date getUpdateTime() {
        return null;
    }

    @Override
    public void setUpdateTime(Date updateTime) {

    }

    @Override
    public String getUpdateBy() {
        return null;
    }

    @Override
    public void setUpdateBy(String updateBy) {

    }
}
