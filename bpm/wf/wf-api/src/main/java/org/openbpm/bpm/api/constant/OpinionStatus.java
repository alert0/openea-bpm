package org.openbpm.bpm.api.constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum OpinionStatus {
    START("start", "提交"),
    CREATE("create", "创建"),
    END("end", "结束"),
    AWAITING_CHECK("awaiting_check", "待审批"),
    AGREE("agree", "同意"),
    OPPOSE("oppose", "反对"),
    ABANDON("abandon", "弃权"),
    REJECT("reject", "驳回"),
    REJECT_TO_START("rejectToStart", "驳回到发起人"),
    REVOKER("revoker", "撤回"),
    REVOKER_TO_START("revoker_to_start", "撤回到发起人"),
    SIGN_PASSED("signPass", "会签通过"),
    SIGN_NOT_PASSED("signNotPass", "会签不通过"),
    SIGN_RECYCLE("signRecycle", "会签回收"),
    SKIP("skip", "跳过执行"),
    TURN("turn", "转办"),
    MANUAL_END("manualEnd", "人工终止");
    
    protected static Logger LOG;
    private String key;
    private String value;

    static {
        LOG = LoggerFactory.getLogger(OpinionStatus.class);
    }

    private OpinionStatus(String key2, String value2) {
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

    public static OpinionStatus fromKey(String key2) {
        OpinionStatus[] values;
        for (OpinionStatus c : values()) {
            if (c.getKey().equalsIgnoreCase(key2)) {
                return c;
            }
        }
        LOG.warn("OpinionStatus 转换失败！ 无法查找到对应的 值：{}", key2);
        return null;
    }

    public static OpinionStatus getByActionName(String actionName) {
        switch (ActionType.fromKey(actionName)) {
            case AGREE:
                return AGREE;
            case SIGNAGREE:
                return SIGN_PASSED;
            case OPPOSE:
                return OPPOSE;
            case SIGNOPPOSE:
                return SIGN_NOT_PASSED;
            case REJECT:
                return REJECT;
            case REJECT2START:
                return REJECT_TO_START;
            case RECOVER:
                return REVOKER;
            case START:
                return START;
            case MANUALEND:
                return MANUAL_END;
            default:
                return AWAITING_CHECK;
        }
    }
}
