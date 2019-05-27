package org.openbpm.bpm.api.constant;

public enum NodeType {
    START("StartNoneEvent", "开始节点"),
    END("EndNoneEvent", "结束节点"),
    USERTASK("UserTask", "用户任务节点"),
    SIGNTASK("SignTask", "会签任务节点"),
    SUBPROCESS("SubProcess", "子流程"),
    CALLACTIVITY("CallActivity", "外部子流程"),
    EXCLUSIVEGATEWAY("ExclusiveGateway", "分支网关"),
    PARALLELGATEWAY("ParallelGateway", "同步网关"),
    INCLUSIVEGATEWAY("InclusiveGateway", "条件网关"),
    SUBSTARTGATEWAY("SubStartGateway", "内嵌子流程开始网关"),
    SUBENDGATEWAY("SubEndGateway", "内嵌子流程结束网关"),
    SUBMULTISTARTGATEWAY("SubMultiStartGateway", "多实例内嵌子流程开始网关"),
    SERVICETASK("ServiceTask", "服务任务节点");
    
    private String key;
    private String value;

    private NodeType(String key2, String value2) {
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

    public static NodeType fromKey(String key2) {
        NodeType[] values;
        for (NodeType c : values()) {
            if (c.getKey().equalsIgnoreCase(key2)) {
                return c;
            }
        }
        throw new IllegalArgumentException(key2);
    }
}
