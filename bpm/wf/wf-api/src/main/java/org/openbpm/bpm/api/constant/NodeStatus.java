package org.openbpm.bpm.api.constant;

import org.openbpm.bpm.api.model.def.NodeProperties;

public enum NodeStatus {
    SUBMIT("submit", "提交"),
    RE_SUBMIT("resubmit", "重新提交"),
    AGREE("agree", "同意"),
    PENDING("pending", "待审批"),
    OPPOSE("oppose", "反对"),
    BACK(NodeProperties.BACK_MODEL_BACK, "驳回"),
    BACK_TO_START("backToStart", "驳回到发起人"),
    COMPLETE("complete", "完成"),
    RECOVER("recover", "撤回"),
    RECOVER_TO_START("recoverToStart", "撤回到发起人"),
    SIGN_PASS("sign_pass", "会签通过"),
    SIGN_NO_PASS("sign_no_pass", "会签不通过"),
    MANUAL_END("manual_end", "人工终止"),
    ABANDON("abandon", "弃权"),
    SUSPEND("suspend", "挂起");
    
    private String key;
    private String value;

    private NodeStatus(String key2, String value2) {
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

    public static NodeStatus fromKey(String key2) {
        NodeStatus[] values;
        for (NodeStatus c : values()) {
            if (c.getKey().equalsIgnoreCase(key2)) {
                return c;
            }
        }
        throw new IllegalArgumentException(key2);
    }
}
