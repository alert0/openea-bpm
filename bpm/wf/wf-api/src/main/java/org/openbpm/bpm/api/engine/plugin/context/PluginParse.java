package org.openbpm.bpm.api.engine.plugin.context;

import com.alibaba.fastjson.JSON;
import org.openbpm.bpm.api.engine.plugin.def.BpmPluginDef;

public interface PluginParse<T extends BpmPluginDef> {
    JSON getJson();

    String getType();

    T parse(JSON json);

    T parse(String str);
}
