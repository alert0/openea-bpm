package org.openbpm.bpm.api.model.def;

import java.io.Serializable;
import org.hibernate.validator.constraints.NotBlank;

public class NodeInit implements Serializable {
    private String beforeShow;
    @NotBlank(message = "节点初始化描述不能为空")
    private String desc = "";
    @NotBlank(message = "节点不能为空")
    private String nodeId = "";
    private String whenSave;

    public String getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(String nodeId2) {
        this.nodeId = nodeId2;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc2) {
        this.desc = desc2;
    }

    public String getBeforeShow() {
        return this.beforeShow;
    }

    public void setBeforeShow(String beforeShow2) {
        this.beforeShow = beforeShow2;
    }

    public String getWhenSave() {
        return this.whenSave;
    }

    public void setWhenSave(String whenSave2) {
        this.whenSave = whenSave2;
    }
}
