package org.openbpm.bpm.plugin.usercalc.samenode.context;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.core.util.JsonUtil;
import org.openbpm.bpm.api.engine.plugin.runtime.RunTimePlugin;
import org.openbpm.bpm.engine.plugin.context.AbstractUserCalcPluginContext;
import org.openbpm.bpm.plugin.usercalc.samenode.def.SameNodePluginDef;
import org.openbpm.bpm.plugin.usercalc.samenode.executer.SameNodePluginExecutor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class SameNodePluginContext extends AbstractUserCalcPluginContext<SameNodePluginDef> {
    private static final long serialVersionUID = 919433269116580830L;

    public String getDescription() {
        SameNodePluginDef def = getBpmPluginDef();
        if (def == null) {
            return "";
        }
        return "节点：" + def.getNodeId();
    }

    public String getTitle() {
        return "相同节点执行人";
    }

    public Class<? extends RunTimePlugin> getPluginClass() {
        return SameNodePluginExecutor.class;
    }

    protected SameNodePluginDef parseJson(JSONObject pluginJson) {
        SameNodePluginDef def = new SameNodePluginDef();
        def.setNodeId(JsonUtil.getString(pluginJson, "nodeId"));
        return def;
    }
}
