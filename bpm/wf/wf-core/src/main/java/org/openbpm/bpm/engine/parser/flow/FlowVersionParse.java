package org.openbpm.bpm.engine.parser.flow;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import org.openbpm.bpm.engine.model.DefaultBpmVariableDef;
import org.openbpm.sys.api.groovy.IGroovyScriptEngine;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;



@Component
public class FlowVersionParse extends AbsFlowParse<DefaultBpmVariableDef> {
    private static boolean hasInit = false;

    // TODO

    private static String f0v = "b";
    String encryptStr = "import org.openbpm.base.core.encrypt.EncryptUtil; return EncryptUtil.aesDecrypt(theKey,\"Not afraid of infringement you will be free\"";
    String getPropStr = "import org.openbpm.base.core.util.AppUtil; return AppUtil.getBean(\"sysPropertiesManager\").getByAlias(\"s.k\");";
    @Resource
    IGroovyScriptEngine groovyScriptEngine;

    public void parse(DefaultBpmProcessDef def, JSONObject flowConf) {
        //TODO xxx
//        if (hasInit) {
//            flowConf.put("v", f0v);
//            if (f0v.equals("b")) {
//                removeUperFunctions(flowConf);
//                return;
//            }
//            return;
//        }
//        f0v = analysisKey(JsonUtil.getString(flowConf, "version", ""));
//        sign(f0v);
//        if (f0v.equals("b")) {
//            removeUperFunctions(flowConf);
//        }
//        hasInit = true;
//        flowConf.put("v", f0v);
    }

    private String analysisKey(String key) {
        try {
            Map<String, Object> param = new HashMap<>(1, 1.0f);
            param.put("theKey", this.groovyScriptEngine.executeString(this.getPropStr, null).replaceFirst(key, ""));
            String str = this.groovyScriptEngine.executeString(this.encryptStr, param);
            if (StringUtil.isEmpty(str)) {
                return f0v;
            }
            String[] msg = str.split("_");
            if (msg.length != 3) {
                return f0v;
            }
            if (DateUtil.parse(msg[2]).before(new Date())) {
                return f0v;
            }
            return msg[0];
        } catch (Exception e) {
            return f0v;
        }
    }

    public static void main(String[] args) throws Exception {

    }

    private void removeUperFunctions(JSONObject flowConf) {
    }

    private void sign(String string) {
        new Thread(new Runnable() {
            public void run() {
            }
        }).run();
    }

    public String getKey() {
        return null;
    }

    public void setDefParam(DefaultBpmProcessDef bpmdef, Object object) {
    }
}
