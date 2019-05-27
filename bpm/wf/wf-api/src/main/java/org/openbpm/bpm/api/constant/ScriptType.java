package org.openbpm.bpm.api.constant;

public enum ScriptType {
    START("start", "开始脚本"),
    END("end", "结束脚本"),
    CREATE("create", "创建脚本"),
    COMPLETE("complete", "结束脚本"),
    MANUALEND("manualEnd", "人工终止");
    
    private String key;
    private String value;

    private ScriptType(String key2, String value2) {
        this.key = "";
        this.value = "";
        this.key = key2;
        this.value = value2;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key2) {
        this.key = key2;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value2) {
        this.value = value2;
    }

    public String toString() {
        return this.key;
    }

    public static ScriptType fromKey(String key2) {
        ScriptType[] values;
        for (ScriptType c : values()) {
            if (c.getKey().equalsIgnoreCase(key2)) {
                return c;
            }
        }
        throw new IllegalArgumentException(key2);
    }
}
