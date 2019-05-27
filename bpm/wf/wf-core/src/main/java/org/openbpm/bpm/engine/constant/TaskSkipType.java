package org.openbpm.bpm.engine.constant;

public enum TaskSkipType {
    NO_SKIP("noSkip", "不跳过"),
    ALL_SKIP("allSkip", "所有节点跳过"),
    FIRSTNODE_SKIP("firstNodeSkip", "开始节点跳过"),
    SAME_USER_SKIP("sameUserSkip", "前一节点相同用户则跳过"),
    USER_EMPTY_SKIP("userEmptySkip", "执行人为空则跳过"),
    SCRIPT_SKIP("scriptSkip", "脚本跳过");
    
    private String key;
    private String value;

    private TaskSkipType(String key2, String value2) {
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

    public static TaskSkipType fromKey(String key2) {
        TaskSkipType[] values;
        for (TaskSkipType c : values()) {
            if (c.getKey().equalsIgnoreCase(key2)) {
                return c;
            }
        }
        throw new IllegalArgumentException(key2);
    }
}
