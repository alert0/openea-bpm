package org.openbpm.bpm.plugin.global.script.executer;

import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import org.openbpm.bpm.engine.plugin.session.BpmExecutionPluginSession;
import org.openbpm.bpm.plugin.global.script.def.NodeScriptPluginDef;
import org.openbpm.sys.api.groovy.IGroovyScriptEngine;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class NodeScriptPluginExecutor extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, NodeScriptPluginDef> {
    @Resource
    IGroovyScriptEngine groovyScriptEngine;

    public Void execute(BpmExecutionPluginSession pluginSession, NodeScriptPluginDef pluginDef) {
        String script = pluginDef.getEvnetnScript(pluginSession.getEventType());
        if (!StringUtil.isEmpty(script)) {
            this.groovyScriptEngine.execute(script, pluginSession);
            this.LOG.info("节点{}执行了{}事件脚本", pluginDef.getNodeId(), pluginSession.getEventType().getValue());
        }
        return null;
    }
}
