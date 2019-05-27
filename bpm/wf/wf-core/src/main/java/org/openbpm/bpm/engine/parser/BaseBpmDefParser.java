package org.openbpm.bpm.engine.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.core.validate.ValidateUtil;
import org.openbpm.bpm.api.engine.plugin.def.BpmDef;
import java.lang.reflect.ParameterizedType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseBpmDefParser<T, D extends BpmDef> implements BpmDefParser<D> {
    protected Logger LOG = LoggerFactory.getLogger(getClass());

    public void parse(D def, JSONObject jsonConf) {
        Object args = null;
        if (jsonConf.containsKey(getKey())) {
            String jsonStr = jsonConf.getString(getKey());
            args = parseDef(def, jsonStr);
            if (args == null) {
                if (isArray()) {
                    args = JSONArray.parseArray(jsonStr, getClazz());
                } else {
                    args = JSON.parseObject(jsonStr, getClazz());
                }
            }
            setDefParam(def, args);
        }
        validate(args);
        JSONAmend(def, args, jsonConf);
    }

    public Object parseDef(D d, String conf) {
        return null;
    }

    public Class<T> getClazz() {
        return (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public boolean isArray() {
        return false;
    }

    public String validate(Object o) {
        if (o != null) {
            ValidateUtil.validate(o);
        }
        return null;
    }

    public void JSONAmend(D d, Object args, JSON configJson) {
    }

    public String getN() {
        return "asdf2dds";
    }
}
