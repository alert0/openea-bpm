package org.openbpm.bpm.api.engine.constant;

public enum LogicType {
    AND("and", "与"),
    OR("or", "或"),
    EXCLUDE("exclude", "非");
    
    private String key;
    private String value;

    private LogicType(String key2, String value2) {
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

    public static LogicType fromKey(String key2) {
        LogicType[] values;
        for (LogicType c : values()) {
            if (c.getKey().equalsIgnoreCase(key2)) {
                return c;
            }
        }
        throw new IllegalArgumentException(key2);
    }
}
