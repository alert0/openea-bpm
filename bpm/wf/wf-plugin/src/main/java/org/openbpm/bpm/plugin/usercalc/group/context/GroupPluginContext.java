package org.openbpm.bpm.plugin.usercalc.group.context;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.core.util.JsonUtil;
import org.openbpm.bpm.api.engine.plugin.def.BpmUserCalcPluginDef;
import org.openbpm.bpm.api.engine.plugin.runtime.RunTimePlugin;
import org.openbpm.bpm.engine.plugin.context.AbstractUserCalcPluginContext;
import org.openbpm.bpm.plugin.usercalc.group.def.GroupPluginDef;
import org.openbpm.bpm.plugin.usercalc.group.executer.GroupPluginExecutor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class GroupPluginContext extends AbstractUserCalcPluginContext {
    private static final long serialVersionUID = -6084686546165511275L;

    public String getDescription() {
        GroupPluginDef def = (GroupPluginDef) getBpmPluginDef();
        if (def == null) {
            return "";
        }
        return String.format("%s[%s]", new Object[]{def.getTypeName(), def.getGroupName()});
    }

    public String getTitle() {
        return "组";
    }

    public Class<? extends RunTimePlugin> getPluginClass() {
        return GroupPluginExecutor.class;
    }

    protected BpmUserCalcPluginDef parseJson(JSONObject pluginJson) {
        GroupPluginDef def = new GroupPluginDef();
        String groupType = JsonUtil.getString(pluginJson, "type");
        String groupTypeName = JsonUtil.getString(pluginJson, "typeName");
        def.setType(groupType);
        def.setTypeName(groupTypeName);
        String groupKey = JsonUtil.getString(pluginJson, "groupKey");
        String groupName = JsonUtil.getString(pluginJson, "groupName");
        def.setGroupKey(groupKey);
        def.setGroupName(groupName);
        return def;
    }
}
