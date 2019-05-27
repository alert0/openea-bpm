package org.openbpm.bpm.plugin.usercalc.group.def;

import org.openbpm.bpm.engine.plugin.plugindef.AbstractUserCalcPluginDef;
import org.hibernate.validator.constraints.NotEmpty;

public class GroupPluginDef extends AbstractUserCalcPluginDef {
    @NotEmpty(message = "人员插件组KEY不能为空")
    private String groupKey = "";
    private String groupName = "";
    @NotEmpty(message = "人员插件组类型不能为空")
    private String type = "";
    private String typeName = "";

    public String getGroupKey() {
        return this.groupKey;
    }

    public void setGroupKey(String groupKey2) {
        this.groupKey = groupKey2;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName2) {
        this.groupName = groupName2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName2) {
        this.typeName = typeName2;
    }
}
