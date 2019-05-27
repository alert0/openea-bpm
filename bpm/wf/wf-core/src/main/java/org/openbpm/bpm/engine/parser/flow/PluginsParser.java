package org.openbpm.bpm.engine.parser.flow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.base.core.validate.ValidateUtil;
import org.openbpm.bpm.api.engine.plugin.context.BpmPluginContext;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class PluginsParser extends AbsFlowParse<BpmPluginContext> {
    public Object parseDef(DefaultBpmProcessDef bpmProcessDef, String json) {
        JSONObject plugins = JSON.parseObject(json);
        setDefaultPlugins(plugins);
        ArrayList<BpmPluginContext> pluginContextList = new ArrayList<>();
        for (String pluginName : plugins.keySet()) {
            BpmPluginContext pluginContext = (BpmPluginContext) AppUtil.getBean(pluginName + "PluginContext");
            if (pluginContext == null) {
                this.LOG.error("插件解析失败，不存在的插件：{}", pluginName + "PluginContext");
                //this.LOG.debug("插件解析失败，不存在的插件：{}", pluginName + "PluginContext", new RuntimeException("插件解析失败"));
            } else {
                if (pluginContext instanceof BpmPluginContext) {
                    try {
                        pluginContext.parse((JSON) plugins.get(pluginName));
                        ValidateUtil.validate(pluginContext.getBpmPluginDef());
                    } catch (Exception e) {
                        this.LOG.error("插件{}解析失败:{}！", new Object[]{pluginContext.getTitle(), e.getMessage(), e});
                    }
                }
                pluginContextList.add(pluginContext);
            }
        }
        return pluginContextList;
    }

    public String getKey() {
        return "plugins";
    }

    public String validate(Object o) {
        return null;
    }

    private void setDefaultPlugins(JSONObject plugins) {
        if (!plugins.containsKey("dataLog")) {
            plugins.put("dataLog", new JSONObject());
        }
    }

    public void setDefParam(DefaultBpmProcessDef bpmProcessDef, Object object) {
        bpmProcessDef.setPluginContextList((ArrayList) object);
    }
}
