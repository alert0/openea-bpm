package org.openbpm.bpm.api.engine.plugin.def;

import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.engine.plugin.context.UserCalcPluginContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserAssignRule implements Comparable<UserAssignRule>, Serializable {
    private static final long serialVersionUID = 1;
    private List<UserCalcPluginContext> calcPluginContextList = new ArrayList();
    private String condition = "";
    private String description = "";
    private int groupNo = 1;
    private String name = "";

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getDescription() {
        if (!StringUtil.isEmpty(this.description)) {
            return this.description;
        }
        String desc = "";
        for (UserCalcPluginContext ctx : this.calcPluginContextList) {
            desc = desc + "　　　【" + ctx.getTitle() + "】" + ctx.getDescription() + ";";
        }
        return desc;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getCondition() {
        return this.condition;
    }

    public void setCondition(String condition2) {
        this.condition = condition2;
    }

    public int getGroupNo() {
        return this.groupNo;
    }

    public void setGroupNo(int groupNo2) {
        this.groupNo = groupNo2;
    }

    public List<UserCalcPluginContext> getCalcPluginContextList() {
        return this.calcPluginContextList;
    }

    public void setCalcPluginContextList(List<UserCalcPluginContext> calcPluginContextList2) {
        this.calcPluginContextList = calcPluginContextList2;
    }

    public int compareTo(UserAssignRule userRule) {
        if (this.groupNo > userRule.groupNo) {
            return 1;
        }
        if (this.groupNo < userRule.groupNo) {
            return -1;
        }
        return 0;
    }
}
