package org.openbpm.bpm.plugin.usercalc.script.def;

import org.openbpm.bpm.engine.plugin.plugindef.AbstractUserCalcPluginDef;
import org.hibernate.validator.constraints.NotEmpty;

public class ScriptPluginDef extends AbstractUserCalcPluginDef {
    @NotEmpty(message = "脚本插件，脚本描述不能为空")
    private String description = "";
    @NotEmpty(message = "脚本插件，脚本不能为空")
    private String script = "";

    public String getScript() {
        return this.script;
    }

    public void setScript(String script2) {
        this.script = script2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }
}
