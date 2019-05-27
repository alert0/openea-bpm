package org.openbpm.bpm.api.constant;

import org.openbpm.bpm.api.model.def.IBpmDefinition.STATUS;
import org.openbpm.bpm.api.model.def.NodeProperties;

public enum InstanceStatus {
    STATUS_DRAFT(STATUS.DRAFT, "草稿"),
    STATUS_RUNNING("running", "运行中"),
    STATUS_END("end", "结束"),
    STATUS_MANUAL_END("manualend", "人工结束"),
    STATUS_BACK(NodeProperties.BACK_MODEL_BACK, "驳回"),
    STATUS_UNDEFINED("undefined", "未定义"),
    STATUS_REVOKE("revoke", "撤销");
    
    private String key;
    private String value;

    private InstanceStatus(String key2, String value2) {
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

    public static InstanceStatus fromKey(String key2) {
        InstanceStatus[] values;
        for (InstanceStatus c : values()) {
            if (c.getKey().equalsIgnoreCase(key2)) {
                return c;
            }
        }
        throw new IllegalArgumentException(key2);
    }

    public static InstanceStatus getByActionName(String actionName) {
        switch (ActionType.fromKey(actionName)) {
            case AGREE:
                return STATUS_RUNNING;
            case OPPOSE:
                return STATUS_RUNNING;
            case REJECT:
                return STATUS_BACK;
            case REJECT2START:
                return STATUS_BACK;
            case RECOVER:
                return STATUS_REVOKE;
            case TASKOPINION:
                return STATUS_RUNNING;
            case MANUALEND:
                return STATUS_MANUAL_END;
            default:
                return STATUS_RUNNING;
        }
    }
}
