package org.openbpm.bpm.plugin.usercalc.script.context;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.core.util.JsonUtil;
import org.openbpm.bpm.api.engine.plugin.runtime.RunTimePlugin;
import org.openbpm.bpm.engine.plugin.context.AbstractUserCalcPluginContext;
import org.openbpm.bpm.plugin.usercalc.script.def.ScriptPluginDef;
import org.openbpm.bpm.plugin.usercalc.script.executer.ScriptPluginExecutor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class ScriptPluginContext extends AbstractUserCalcPluginContext<ScriptPluginDef> {
    private static final long serialVersionUID = -2353875054502587417L;

    public String getDescription() {
        ScriptPluginDef def = getBpmPluginDef();
        if (def == null) {
            return "";
        }
        return def.getDescription();
    }

    public Class<? extends RunTimePlugin> getPluginClass() {
        return ScriptPluginExecutor.class;
    }

    public String getTitle() {
        return "脚本";
    }

    protected ScriptPluginDef parseJson(JSONObject pluginJson) {
        ScriptPluginDef def = new ScriptPluginDef();
        String script = pluginJson.getString("script");
        String description = JsonUtil.getString(pluginJson, "description", "脚本");
        def.setScript(script);
        def.setDescription(description);
        return def;
    }
}
