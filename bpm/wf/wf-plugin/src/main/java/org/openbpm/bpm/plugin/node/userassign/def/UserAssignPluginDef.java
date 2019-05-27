package org.openbpm.bpm.plugin.node.userassign.def;

import org.openbpm.bpm.api.engine.plugin.def.UserAssignRule;
import org.openbpm.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

public class UserAssignPluginDef extends AbstractBpmExecutionPluginDef {
    @Valid
    List<UserAssignRule> ruleList = new ArrayList();

    public List<UserAssignRule> getRuleList() {
        return this.ruleList;
    }

    public void setRuleList(List<UserAssignRule> ruleList2) {
        this.ruleList = ruleList2;
    }
}
