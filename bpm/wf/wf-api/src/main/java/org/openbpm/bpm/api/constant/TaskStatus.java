package org.openbpm.bpm.api.constant;

public enum TaskStatus {
    NORMAL("NORMAL", "普通", "普通订单"),
    SUSPEND("SUSPEND", "挂起", "超管挂起任务"),
    LOCK("LOCK", "锁定", "个人将任务锁定至个人名下"),
    TURN("TURN", "转办", "个人将任务转办给其他人"),
    AGENCY("AGENCY", "代理", "代理其他人的任务"),
    BACK("BACK", "驳回", "被驳回的任务"),
    DESIGNATE("DESIGNATE", "指派", "个人将任务指派到某个人名下"),
    DRAG("DRAG", "捞单", "从捞单池中获取的订单");
    
    private String desc;
    private String key;
    private String value;

    private TaskStatus(String key2, String value2, String desc2) {
        this.key = "";
        this.value = "";
        this.desc = "";
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

    public static TaskStatus fromKey(String key2) {
        TaskStatus[] values;
        for (TaskStatus c : values()) {
            if (c.getKey().equalsIgnoreCase(key2)) {
                return c;
            }
        }
        throw new IllegalArgumentException(key2);
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc2) {
        this.desc = desc2;
    }
}
