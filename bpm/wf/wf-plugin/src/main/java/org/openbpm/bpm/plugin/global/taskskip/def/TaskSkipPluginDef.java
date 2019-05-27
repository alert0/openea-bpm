package org.openbpm.bpm.plugin.global.taskskip.def;

import org.openbpm.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;

public class TaskSkipPluginDef extends AbstractBpmExecutionPluginDef {
    private String script = "";
    private String skipTypeArr;

    public String getScript() {
        return this.script;
    }

    public void setScript(String script2) {
        this.script = script2;
    }

    public String getSkipTypeArr() {
        return this.skipTypeArr;
    }

    public void setSkipTypeArr(String skipTypeArr2) {
        this.skipTypeArr = skipTypeArr2;
    }
}
