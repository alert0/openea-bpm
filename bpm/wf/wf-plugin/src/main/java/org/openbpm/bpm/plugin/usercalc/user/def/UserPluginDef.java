package org.openbpm.bpm.plugin.usercalc.user.def;

import org.openbpm.bpm.engine.plugin.plugindef.AbstractUserCalcPluginDef;

public class UserPluginDef extends AbstractUserCalcPluginDef {
    private String account = "";
    private String source = "";
    private String userName = "";

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account2) {
        this.account = account2;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName2) {
        this.userName = userName2;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source2) {
        this.source = source2;
    }
}
