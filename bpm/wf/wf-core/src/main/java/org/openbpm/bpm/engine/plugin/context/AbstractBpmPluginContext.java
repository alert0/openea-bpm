package org.openbpm.bpm.engine.plugin.context;

import com.alibaba.fastjson.JSON;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.engine.plugin.context.BpmPluginContext;
import org.openbpm.bpm.api.engine.plugin.def.BpmPluginDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBpmPluginContext<T extends BpmPluginDef> implements BpmPluginContext<T> {
    private static final long serialVersionUID = 1;
    protected Logger LOG = LoggerFactory.getLogger(getClass());
    private T bpmPluginDef;
    protected int sn = 100;

    public java.lang.Integer getSn() {
       return this.sn;
    }

    protected abstract T parseFromJson(JSON json);

    public T getBpmPluginDef() {
        return this.bpmPluginDef;
    }

    public void setBpmPluginDef(T bpmPluginDef2) {
        this.bpmPluginDef = bpmPluginDef2;
    }

    public JSON getJson() {
        return (JSON) JSON.toJSON(this.bpmPluginDef);
    }

    public T parse(JSON json) {
        setBpmPluginDef(parseFromJson(json));
        return this.bpmPluginDef;
    }

    public T parse(String json) {
        if (StringUtil.isEmpty(json)) {
            return null;
        }
        return parse((JSON) JSON.parse(json));
    }

    public String getType() {
        return StringUtil.lowerFirst(getClass().getSimpleName().replaceAll("PluginContext", ""));
    }

    public String persistnce(String defId) {
        if (getJson() == null) {
            return "清空改插件";
        }
        return null;
    }

    public int compareTo(BpmPluginContext pluginContext) {
        if (this.sn != pluginContext.getSn().intValue() && this.sn > pluginContext.getSn().intValue()) {
            return 1;
        }
        return 0;
    }
}
