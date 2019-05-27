package org.openbpm.bpm.plugin.node.ruleskip.def;

import java.io.Serializable;
import org.hibernate.validator.constraints.NotEmpty;

public class JumpRule implements Serializable {
    @NotEmpty(message = "跳转规则名字不能为空")
    private String name = "";
    @NotEmpty(message = "跳转规则条件不能为空")
    private String script = "";
    @NotEmpty(message = "跳转规则目标节点不能为空")
    private String targetNode = "";

    public JumpRule() {
    }

    public JumpRule(String ruleName, String targetNode2, String condition) {
        this.name = ruleName;
        this.targetNode = targetNode2;
        this.script = condition;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String ruleName) {
        this.name = ruleName;
    }

    public String getTargetNode() {
        return this.targetNode;
    }

    public void setTargetNode(String targetNode2) {
        this.targetNode = targetNode2;
    }

    public String getScript() {
        return this.script;
    }

    public void setScript(String script2) {
        this.script = script2;
    }
}
