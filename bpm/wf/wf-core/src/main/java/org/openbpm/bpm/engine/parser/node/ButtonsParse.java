package org.openbpm.bpm.engine.parser.node;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.model.nodedef.Button;
import org.openbpm.bpm.api.model.nodedef.impl.BaseBpmNodeDef;
import org.openbpm.bpm.engine.parser.FlowConfigConstants.NODE;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ButtonsParse extends AbsNodeParse<Button> {
    public String getKey() {
        return NODE.BUTTON_LIST;
    }

    public void setDefParam(BaseBpmNodeDef userNodeDef, Object object) {
        userNodeDef.setButtons((List) object);
    }

    public boolean isArray() {
        return true;
    }

    public void JSONAmend(BaseBpmNodeDef userNodeDef, Object object, JSON thisJson) {
        JSONObject jsonConfig = (JSONObject) thisJson;
        if (isEmpty(object)) {
            jsonConfig.put(NODE.BUTTON_LIST, JSON.toJSON(userNodeDef.getButtons()));
        }
    }

    private boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            return StringUtil.isEmpty((String) object);
        }
        if (object instanceof Collection) {
            return CollectionUtil.isEmpty((Collection) object);
        }
        if (object.getClass().isArray()) {
            return ArrayUtil.isEmpty((Object[]) object);
        }
        if (object instanceof Map) {
            return MapUtil.isEmpty((Map) object);
        }
        if (object instanceof JSON) {
            return ((JSONObject) object).isEmpty();
        }
        if (object instanceof JSONArray) {
            return ((JSONArray) object).isEmpty();
        }
        return false;
    }
}
