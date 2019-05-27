package org.openbpm.bpm.plugin.node.ruleskip.def;

import org.openbpm.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

public class RuleSkipPluginDef extends AbstractBpmExecutionPluginDef {
    @Valid
    private List<JumpRule> jumpRules = new ArrayList();

    public List<JumpRule> getJumpRules() {
        return this.jumpRules;
    }

    public void setJumpRules(List<JumpRule> jumpRules2) {
        this.jumpRules = jumpRules2;
    }
}
