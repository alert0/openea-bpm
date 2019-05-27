package org.openbpm.bpm.api.constant;

public enum TaskType {
    NORMAL("NORMAL", "普通任务"),
    SIGN("SIGN", "会签任务"),
    SIGN_SOURCE("SIGN_SOURCE", "会签任务_源"),
    SUBFLOW("SUBFLOW", "子流程任务"),
    AGENT("AGENT", "代理任务"),
    DELIVERTO("DELIVERTO", "转办任务"),
    TRANSFORMING("TRANSFORMING", "事项任务");
    
    private String key;
    private String value;

    private TaskType(String key2, String value2) {
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

    public boolean equalsWithKey(String key2) {
        return this.key.equals(key2);
    }

    public static TaskType fromKey(String key2) {
        TaskType[] values;
        for (TaskType c : values()) {
            if (c.getKey().equalsIgnoreCase(key2)) {
                return c;
            }
        }
        throw new IllegalArgumentException(key2);
    }
}
