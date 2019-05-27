package org.openbpm.bpm.plugin.usercalc.approver.context;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.bpm.api.engine.plugin.runtime.RunTimePlugin;
import org.openbpm.bpm.engine.plugin.context.AbstractUserCalcPluginContext;
import org.openbpm.bpm.plugin.usercalc.approver.def.ApproverPluginDef;
import org.openbpm.bpm.plugin.usercalc.approver.executer.ApproverPluginExecutor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class ApproverPluginContext extends AbstractUserCalcPluginContext<ApproverPluginDef> {
    private static final long serialVersionUID = 2164348894650802414L;

    public String getDescription() {
        return "流程历史审批人";
    }

    public String getTitle() {
        return "流程历史审批人";
    }

    public Class<? extends RunTimePlugin> getPluginClass() {
        return ApproverPluginExecutor.class;
    }

    protected ApproverPluginDef parseJson(JSONObject pluginJson) {
        return new ApproverPluginDef();
    }
}
