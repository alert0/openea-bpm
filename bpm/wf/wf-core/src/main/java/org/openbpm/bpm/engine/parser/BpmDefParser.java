package org.openbpm.bpm.engine.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.bpm.api.engine.plugin.def.BpmDef;

interface BpmDefParser<D extends BpmDef> {
    void JSONAmend(D d, Object obj, JSON json);

    Class getClazz();

    String getKey();

    boolean isArray();

    void parse(D d, JSONObject jSONObject);

    Object parseDef(D d, String str);

    void setDefParam(D d, Object obj);

    String validate(Object obj);
}
