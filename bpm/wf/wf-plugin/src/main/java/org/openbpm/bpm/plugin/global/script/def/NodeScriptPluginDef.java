package org.openbpm.bpm.plugin.global.script.def;

import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.validator.constraints.NotEmpty;

public class NodeScriptPluginDef extends AbstractBpmExecutionPluginDef {
    @NotEmpty(message = "事件脚本节点ID不能为空")
    private String nodeId = "";
    private Map<EventType, String> script = new HashMap();

    public String getEvnetnScript(EventType event) {
        return (String) this.script.get(event);
    }

    public void setEvnetnScript(EventType event, String scritp) {
        this.script.put(event, scritp);
    }

    public Map<EventType, String> getScript() {
        return this.script;
    }

    public void setScript(Map<EventType, String> script2) {
        this.script = script2;
    }

    public String getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(String nodeId2) {
        this.nodeId = nodeId2;
    }
}
