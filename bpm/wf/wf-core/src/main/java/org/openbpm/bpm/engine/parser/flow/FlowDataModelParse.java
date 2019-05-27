package org.openbpm.bpm.engine.parser.flow;

import org.openbpm.bpm.api.model.def.BpmDataModel;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import org.openbpm.bpm.engine.parser.FlowConfigConstants.GLOBAL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class FlowDataModelParse extends AbsFlowParse<BpmDataModel> {
    public String getKey() {
        return GLOBAL.DATA_MODEL_LIST;
    }

    public String validate(Object object) {
        List<BpmDataModel> list = (List) object;
        Set<String> keys = new HashSet<>();
        for (BpmDataModel def : list) {
            String key = def.getCode();
            if (keys.contains(key)) {
                throw new RuntimeException("解析流程数据模型出错code：" + key + "在流程中重复配置！");
            }
            keys.add(def.getCode());
        }
        return "";
    }

    public void setDefParam(DefaultBpmProcessDef bpmProcessDef, Object object) {
        bpmProcessDef.setDataModelList((List) object);
    }

    public boolean isArray() {
        return true;
    }
}
