package org.openbpm.bpm.engine.parser.node;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.bpm.api.engine.plugin.context.BpmPluginContext;
import org.openbpm.bpm.api.model.nodedef.impl.BaseBpmNodeDef;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class NodePluginsParser extends AbsNodeParse<BpmPluginContext> {
    public Object parseDef(BaseBpmNodeDef userNodeDef, String json) {
        JSONObject plugins = JSON.parseObject(json);
        ArrayList<BpmPluginContext> pluginContextList = new ArrayList<>();
        for (String pluginName : plugins.keySet()) {
            BpmPluginContext pluginContext = (BpmPluginContext) AppUtil.getBean(pluginName + "PluginContext");
            if (pluginContext == null) {
                this.LOG.error("插件解析失败，不存在的插件：{}", pluginName + "PluginContext");
            } else {
                if (pluginContext instanceof BpmPluginContext) {
                    pluginContext.parse((JSON) plugins.get(pluginName));
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

    public void setDefParam(BaseBpmNodeDef userNodeDef, Object object) {
        userNodeDef.setBpmPluginContexts((ArrayList) object);
    }
}
