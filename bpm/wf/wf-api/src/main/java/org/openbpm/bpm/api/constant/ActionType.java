package org.openbpm.bpm.api.constant;

import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.model.def.IBpmDefinition.STATUS;

public enum ActionType {
    DRAFT(STATUS.DRAFT, "保存草稿", "instanceSaveActionHandler"),
    START("start", "启动", "instanceStartActionHandler"),
    AGREE("agree", "同意", "taskAgreeActionHandler"),
    SIGNAGREE("signAgree", "会签同意", "taskSignAgreeActionHandler"),
    SAVE("save", "保存", "taskSaveActionHandler"),
    OPPOSE("oppose", "反对", "taskOpposeActionHandler"),
    SIGNOPPOSE("signOppose", "会签反对", "taskSignOpposeActionHandler"),
    REJECT("reject", "驳回", "taskRejectActionHandler"),
    REJECT2START("reject2Start", "驳回发起人", "taskReject2StartActionHandler"),
    RECOVER("recover", "撤销", "null"),
    DISPENDSE("dispense", "分发", "null"),
    TASKOPINION("taskOpinion", "审批历史", "instanceTaskOpinionActionHandler"),
    FLOWIMAGE("flowImage", "流程图", "instanceImageActionHandler"),
    PRINT("print", "打印", "instancePrintActionHandler"),
    MANUALEND("manualEnd", "人工终止", "instanceManualEndActionHandler"),
    LOCK("lock", "锁定", "taskLockActionHandler"),
    UNLOCK("unlock", "解锁", "taskUnlockActionHandler"),
    TURN("turn", "转办", "taskTurnActionHandler"),
    REMINDER("reminder", "催办", "instanceReminderActionHandler"),
    WITHDRAW("withdraw", "撤回", "instanceWithdrawActionHandler"),
    CREATE("create", "创建时", "null"),
    END("end", "流程结束", "null");
    
    private String beanId;
    private String key;
    private String name;

    private ActionType(String key2, String name2, String beanId2) {
        this.key = "";
        this.name = "";
        this.beanId = "";
        this.key = key2;
        this.name = name2;
        this.beanId = beanId2;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key2) {
        this.key = key2;
    }

    public String getName() {
        return this.name;
    }

    public String getBeanId() {
        return this.beanId;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String toString() {
        return this.key;
    }

    public static ActionType fromKey(String key2) {
        ActionType[] values;
        for (ActionType c : values()) {
            if (c.getKey().equalsIgnoreCase(key2)) {
                return c;
            }
        }
        throw new BusinessException(BpmStatusCode.NO_TASK_ACTION);
    }
}
