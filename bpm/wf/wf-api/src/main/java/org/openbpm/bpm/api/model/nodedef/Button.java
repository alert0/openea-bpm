package org.openbpm.bpm.api.model.nodedef;

import java.io.Serializable;

public class Button implements Serializable {
    private static final long serialVersionUID = 1;
    protected String afterScript = "";
    protected String alias = "";
    protected String beforeScript = "";
    protected String configPage = "";
    protected String groovyScript = "";
    protected String name = "";

    public Button() {
    }

    public Button(String name2, String alias2) {
        this.name = name2;
        this.alias = alias2;
    }

    public Button(String name2, String alias2, String groovyScript2, String configPage2) {
        this.name = name2;
        this.alias = alias2;
        this.groovyScript = groovyScript2;
        this.configPage = configPage2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias2) {
        this.alias = alias2;
    }

    public String getBeforeScript() {
        return this.beforeScript;
    }

    public void setBeforeScript(String beforeScript2) {
        this.beforeScript = beforeScript2;
    }

    public String getAfterScript() {
        return this.afterScript;
    }

    public void setAfterScript(String afterScript2) {
        this.afterScript = afterScript2;
    }

    public String getGroovyScript() {
        return this.groovyScript;
    }

    public void setGroovyScript(String groovyScript2) {
        this.groovyScript = groovyScript2;
    }

    public String getConfigPage() {
        return this.configPage;
    }

    public void setConfigPage(String configPage2) {
        this.configPage = configPage2;
    }

    public String toString() {
        return "[name=" + this.name + ", alias=" + this.alias + "]";
    }
}
