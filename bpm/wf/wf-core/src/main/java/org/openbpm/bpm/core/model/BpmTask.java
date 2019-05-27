package org.openbpm.bpm.core.model;

import lombok.Data;
import org.openbpm.base.api.model.IBaseModel;
import org.openbpm.bpm.api.model.task.IBpmTask;

import java.util.Date;

@Data
public class BpmTask implements IBaseModel, IBpmTask {


    protected String actExecutionId;
    protected String actInstId;
    protected String assigneeId;
    protected String assigneeNames;
    protected String backNode;
    protected String createBy;
    protected Date createTime;
    protected String defId;
    protected Date dueTime;
    protected String id;
    protected String instId;
    protected String name;
    protected String nodeId;
    protected String parentId;
    protected Integer priority;
    protected String status;
    protected String subject;
    protected Integer supportMobile;
    protected String taskId;
    protected String taskType;
    protected String typeId;


    /**
     * <pre>
     * 更新时间
     * </pre>
     *
     * @return
     */
    @Override
    public Date getUpdateTime() {
        return null;
    }

    /**
     * <pre>
     * 设置 更新时间
     * </pre>
     *
     * @param updateTime
     */
    @Override
    public void setUpdateTime(Date updateTime) {

    }

    /**
     * <pre>
     * 操作人
     * </pre>
     *
     * @return
     */
    @Override
    public String getUpdateBy() {
        return null;
    }

    /**
     * <pre>
     * 设置更新人ID
     * </pre>
     *
     * @param updateBy
     */
    @Override
    public void setUpdateBy(String updateBy) {

    }

    @Override
    public String getActExecutionIdId() {
        return this.actExecutionId;
    }
}
