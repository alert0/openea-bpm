package org.openbpm.bpm.api.model.def;

import java.io.Serializable;

public class BpmDataModel implements Serializable {
    private String code = "";
    private String name = "";

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code2) {
        this.code = code2;
    }
}
