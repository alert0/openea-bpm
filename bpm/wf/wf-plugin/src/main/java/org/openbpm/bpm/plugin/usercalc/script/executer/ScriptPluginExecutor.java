package org.openbpm.bpm.plugin.usercalc.script.executer;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
import org.openbpm.bpm.engine.plugin.session.BpmUserCalcPluginSession;
import org.openbpm.bpm.plugin.usercalc.script.def.ScriptPluginDef;
import org.openbpm.sys.api.groovy.IGroovyScriptEngine;
import org.openbpm.sys.api.model.SysIdentity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ScriptPluginExecutor extends AbstractUserCalcPlugin<ScriptPluginDef> {
    @Resource
    IGroovyScriptEngine groovyScriptEngine;

    public List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, ScriptPluginDef def) {
        String script = def.getScript();
        if (StringUtil.isEmpty(script)) {
            return Collections.EMPTY_LIST;
        }
        Set<SysIdentity> set = (Set) this.groovyScriptEngine.executeObject(script, pluginSession);
        List<SysIdentity> list = new ArrayList<>();
        if (CollectionUtil.isEmpty(set)) {
            return list;
        }
        list.addAll(set);
        return list;
    }

    public boolean supportPreView() {
        return false;
    }
}
