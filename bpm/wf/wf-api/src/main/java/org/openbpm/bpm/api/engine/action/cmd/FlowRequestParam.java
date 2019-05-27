package org.openbpm.bpm.api.engine.action.cmd;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.form.api.model.FormCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel(description = "流程实例、任务 ActionCmd 请求参数，请参考FlowRequestParam.java 或者文档了解 ", value = "流程 ActionCmd 请求参数")
public class FlowRequestParam {
    @NotBlank
    @ApiModelProperty("action name 必须")
    private String action;
    @ApiModelProperty("流程业务主键。 URL表单，可以直接赋值调用rest接口启动流程")
    private String businessKey;
    @ApiModelProperty("流程业务数据，JSON格式：{boCodeA:{},boCodeB:{}}")
    private JSONObject data;
    @ApiModelProperty("流程定义id，流程启动等场景必须")
    private String defId;
    @ApiModelProperty("目标节点")
    private String destination;
    @ApiModelProperty("特殊属性扩展配置：可以再 ActionCmd 中拿到此配置")
    private JSONObject extendConf;
    @ApiModelProperty("表单类型")
    private String formType = FormCategory.INNER.value();
    @ApiModelProperty("流程实例id，流程草稿等场景")
    private String instanceId;
    @ApiModelProperty("前端节点人员设置")
    private JSONObject nodeUsers;
    @ApiModelProperty("流程任务审批意见")
    private String opinion;
    @ApiModelProperty("流程任务id，流程任务处理时必须")
    private String taskId;

    public FlowRequestParam(String taskId2, String action2, JSONObject data2, String opinion2) {
        this.taskId = taskId2;
        this.action = action2;
        this.data = data2;
        this.opinion = opinion2;
    }

    public FlowRequestParam(String defId2, String action2, JSONObject data2) {
        this.defId = defId2;
        this.action = action2;
        this.data = data2;
    }

    public FlowRequestParam() {
    }

    public String getDefId() {
        return this.defId;
    }

    public void setDefId(String defId2) {
        this.defId = defId2;
    }

    public String getInstanceId() {
        return this.instanceId;
    }

    public void setInstanceId(String instanceId2) {
        this.instanceId = instanceId2;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId2) {
        this.taskId = taskId2;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action2) {
        this.action = action2;
    }

    public String getBusinessKey() {
        return this.businessKey;
    }

    public void setBusinessKey(String businessKey2) {
        this.businessKey = businessKey2;
    }

    public JSONObject getNodeUsers() {
        return this.nodeUsers;
    }

    public void setNodeUsers(JSONObject nodeUsers2) {
        this.nodeUsers = nodeUsers2;
    }

    public JSONObject getData() {
        return this.data;
    }

    public void setData(JSONObject data2) {
        this.data = data2;
    }

    public String getFormType() {
        return this.formType;
    }

    public void setFormType(String formType2) {
        this.formType = formType2;
    }

    public String getOpinion() {
        return this.opinion;
    }

    public void setOpinion(String opinion2) {
        this.opinion = opinion2;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination2) {
        this.destination = destination2;
    }

    public JSONObject getExtendConf() {
        return this.extendConf;
    }

    public void setExtendConf(JSONObject extendConf2) {
        this.extendConf = extendConf2;
    }
}
