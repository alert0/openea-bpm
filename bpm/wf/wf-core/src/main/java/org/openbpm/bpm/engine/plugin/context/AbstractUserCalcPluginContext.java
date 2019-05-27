package org.openbpm.bpm.engine.plugin.context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.constant.ExtractType;
import org.openbpm.bpm.api.engine.constant.LogicType;
import org.openbpm.bpm.api.engine.plugin.context.PluginParse;
import org.openbpm.bpm.api.engine.plugin.context.UserCalcPluginContext;
import org.openbpm.bpm.api.engine.plugin.def.BpmUserCalcPluginDef;

public abstract class AbstractUserCalcPluginContext<T extends BpmUserCalcPluginDef> implements UserCalcPluginContext, PluginParse<T> {
    private static final long serialVersionUID = -4447195857368619545L;
    private T bpmPluginDef;

    protected abstract T parseJson(JSONObject jSONObject);

    public T getBpmPluginDef() {
        return this.bpmPluginDef;
    }

    public void setBpmPluginDef(T bpmPluginDef2) {
        this.bpmPluginDef = bpmPluginDef2;
    }

    public T parse(JSON pluginDefJson) {
        JSONObject jsonObject = (JSONObject) pluginDefJson;
        T bpmPluginDef2 = parseJson(jsonObject);
        String extract = jsonObject.getString("extract");
        String logicCal = jsonObject.getString("logicCal");
        bpmPluginDef2.setExtract(ExtractType.fromKey(extract));
        bpmPluginDef2.setLogicCal(LogicType.fromKey(logicCal));
        setBpmPluginDef(bpmPluginDef2);
        return bpmPluginDef2;
    }

    public T parse(String jsonStr) {
        return parse((JSON) JSON.parse(jsonStr));
    }

    public JSON getJson() {
        return (JSON) JSON.toJSON(this.bpmPluginDef);
    }

    public String getType() {
        return StringUtil.lowerFirst(getClass().getSimpleName().replaceAll("PluginContext", ""));
    }
}
