package org.openbpm.bpm.plugin.global.script.context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.engine.plugin.runtime.RunTimePlugin;
import org.openbpm.bpm.engine.plugin.context.AbstractBpmPluginContext;
import org.openbpm.bpm.plugin.global.script.def.NodeScriptPluginDef;
import org.openbpm.bpm.plugin.global.script.executer.NodeScriptPluginExecutor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class NodeScriptPluginContext extends AbstractBpmPluginContext<NodeScriptPluginDef> {
    private static final long serialVersionUID = -5958682303600423597L;

    public List<EventType> getEventTypes() {
        List<EventType> list = new ArrayList<>();
        list.add(EventType.START_EVENT);
        list.add(EventType.END_EVENT);
        list.add(EventType.TASK_COMPLETE_EVENT);
        list.add(EventType.TASK_CREATE_EVENT);
        list.add(EventType.MANUAL_END);
        return list;
    }

    public Class<? extends RunTimePlugin> getPluginClass() {
        return NodeScriptPluginExecutor.class;
    }

    protected NodeScriptPluginDef parseFromJson(JSON pluginJson) {
        JSONObject jsonObject = (JSONObject) pluginJson;
        NodeScriptPluginDef def = new NodeScriptPluginDef();
        for (String key : jsonObject.keySet()) {
            try {
                def.setEvnetnScript(EventType.fromKey(key), jsonObject.getString(key));
            } catch (Exception e) {
            }
        }
        return def;
    }

    public String getTitle() {
        return "脚本";
    }
}
