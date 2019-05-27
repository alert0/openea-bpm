package org.openbpm.bpm.api.model.def;

import org.openbpm.bpm.api.constant.ScriptType;

public class EventScript {
    private String content = "";
    private ScriptType scriptType;

    public EventScript() {
    }

    public EventScript(ScriptType scriptType2, String content2) {
        this.scriptType = scriptType2;
        this.content = content2;
    }

    public ScriptType getScriptType() {
        return this.scriptType;
    }

    public void setScriptType(ScriptType scriptType2) {
        this.scriptType = scriptType2;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content2) {
        this.content = content2;
    }
}
