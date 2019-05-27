package org.openbpm.bpm.api.constant;

public enum EventType {
    START_EVENT("startEvent", "流程启动事件"),
    START_POST_EVENT("postStartEvent", "流程启动后置事件"),
    END_EVENT("endEvent", "流程结束事件"),
    MANUAL_END("manualEnd", "流程人工终止事件"),
    END_POST_EVENT("postEndEvent", "流程结束后置事件"),
    TASK_PRE_COMPLETE_EVENT("preTaskComplete", "任务完成前置事件"),
    TASK_CREATE_EVENT("taskCreate", "任务创建事件"),
    TASK_POST_CREATE_EVENT("postTaskCreate", "任务创建后置事件"),
    TASK_COMPLETE_EVENT("taskComplete", "任务完成事件"),
    TASK_POST_COMPLETE_EVENT("postTaskComplete", "任务完成后置事件"),
    TASK_SIGN_CREATE_EVENT("taskSignCreate", "会签任务创建"),
    TASK_SIGN_POST_CREATE_EVENT("postTaskSignCreate", "会签任务创建后置事件");
    
    private String key;
    private String value;

    private EventType(String key2, String value2) {
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

    public static EventType fromKey(String key2) {
        EventType[] values;
        for (EventType c : values()) {
            if (c.getKey().equalsIgnoreCase(key2)) {
                return c;
            }
        }
        throw new IllegalArgumentException(key2);
    }
}
