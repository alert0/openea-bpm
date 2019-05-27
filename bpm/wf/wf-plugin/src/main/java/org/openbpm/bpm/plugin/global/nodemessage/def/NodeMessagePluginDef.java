package org.openbpm.bpm.plugin.global.nodemessage.def;

import org.openbpm.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
import java.util.List;
import javax.validation.Valid;
import org.hibernate.validator.constraints.NotEmpty;

public class NodeMessagePluginDef extends AbstractBpmExecutionPluginDef {
    @Valid
    @NotEmpty
    private List<NodeMessage> nodeMessageList;

    public NodeMessagePluginDef(List<NodeMessage> nodeMessageList2) {
        this.nodeMessageList = nodeMessageList2;
    }

    public List<NodeMessage> getNodeMessageList() {
        return this.nodeMessageList;
    }

    public void setNodeMessageList(List<NodeMessage> nodeMessageList2) {
        this.nodeMessageList = nodeMessageList2;
    }
}
