package org.openbpm.bpm.api.engine.data.result;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.bpm.api.model.form.BpmForm;
import org.openbpm.bpm.api.model.nodedef.Button;
import org.openbpm.bus.api.model.IBusinessData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;

@ApiModel("流程数据")
public class BpmFlowData implements FlowData {
    @ApiModelProperty("流程当前节点表单")
    protected BpmForm Form;
    @ApiModelProperty("流程 当前节点按钮信息")
    protected List<Button> buttonList;
    @ApiModelProperty("流程自定义表单业务数据")
    protected JSONObject data;
    transient Map<String, IBusinessData> dataMap;
    @ApiModelProperty("流程定义ID")
    protected String defId;
    @ApiModelProperty("流程定义名字")
    protected String defName;
    @ApiModelProperty("流程自定义表单 初始化数据，可用于子表数据复制赋值")
    protected JSONObject initData;
    @ApiModelProperty("流程自定义表单 权限")
    protected JSONObject permission;
    @ApiModelProperty("流程自定义表单 表权限")
    protected JSONObject tablePermission;

    public String getDefId() {
        return this.defId;
    }

    public void setDefId(String defId2) {
        this.defId = defId2;
    }

    public BpmForm getForm() {
        return this.Form;
    }

    public void setForm(BpmForm Form2) {
        this.Form = Form2;
    }

    public JSONObject getPermission() {
        return this.permission;
    }

    public JSONObject getTablePermission() {
        return this.tablePermission;
    }

    public void setTablePermission(JSONObject tablePermission2) {
        this.tablePermission = tablePermission2;
    }

    public void setPermission(JSONObject permission2) {
        this.permission = permission2;
    }

    public JSONObject getData() {
        return this.data;
    }

    public String getDefName() {
        return this.defName;
    }

    public void setDefName(String defName2) {
        this.defName = defName2;
    }

    public void setData(JSONObject dataModel) {
        this.data = dataModel;
    }

    public List<Button> getButtonList() {
        return this.buttonList;
    }

    public Map<String, IBusinessData> getDataMap() {
        return this.dataMap;
    }

    public void setDataMap(Map<String, IBusinessData> dataMap2) {
        this.dataMap = dataMap2;
    }

    public void setButtonList(List<Button> list) {
        this.buttonList = list;
    }

    public JSONObject getInitData() {
        return this.initData;
    }

    public void setInitData(JSONObject initData2) {
        this.initData = initData2;
    }
}
