package org.openbpm.bpm.engine.parser.flow;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.bpm.api.model.def.BpmDefProperties;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import org.openbpm.bpm.engine.parser.FlowConfigConstants.GLOBAL;
import org.springframework.stereotype.Component;

@Component
public class FlowPropertiesParse extends AbsFlowParse<BpmDefProperties> {
    public void parse(DefaultBpmProcessDef def, JSONObject flowConf) {
        JSONObject properties = (JSONObject) JSONObject.toJSON(def.getExtProperties());
        if (flowConf.containsKey(getKey())) {
            properties = flowConf.getJSONObject(getKey());
        }
        def.setExtProperties((BpmDefProperties) JSONObject.toJavaObject(properties, BpmDefProperties.class));
        flowConf.put(getKey(), properties);
    }

    public String getKey() {
        return GLOBAL.PROPERTIES;
    }

    public void setDefParam(DefaultBpmProcessDef bpmProcessDef, Object object) {
        bpmProcessDef.setExtProperties((BpmDefProperties) object);
    }
}
