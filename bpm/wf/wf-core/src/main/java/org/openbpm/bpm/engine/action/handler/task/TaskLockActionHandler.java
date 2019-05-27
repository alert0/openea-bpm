package org.openbpm.bpm.engine.action.handler.task;

import org.openbpm.base.api.exception.BusinessMessage;
import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.constant.TaskStatus;
import org.openbpm.bpm.core.model.BpmTask;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.openbpm.sys.util.ContextUtil;
import org.springframework.stereotype.Component;

@Component
public class TaskLockActionHandler extends AbstractTaskActionHandler<DefualtTaskActionCmd> {
    public ActionType getActionType() {
        return ActionType.LOCK;
    }

    public void execute(DefualtTaskActionCmd model) {
        prepareActionDatas(model);
        checkFlowIsValid(model);
        BpmTask task = (BpmTask) model.getBpmTask();
        if (!task.getAssigneeId().equals("0")) {
            throw new BusinessMessage("该任务只有一个候选人没有锁定的必要。");
        }
        task.setAssigneeId(ContextUtil.getCurrentUserId());
        task.setAssigneeNames(ContextUtil.getCurrentUser().getFullname());
        task.setStatus(TaskStatus.LOCK.getKey());
        this.taskManager.update(task);
    }

    /* access modifiers changed from: protected */
    public void doActionAfter(DefualtTaskActionCmd actionModel) {
    }

    /* access modifiers changed from: protected */
    public void doActionBefore(DefualtTaskActionCmd actionModel) {
    }

    public int getSn() {
        return 6;
    }

    public Boolean isDefault() {
        return Boolean.valueOf(false);
    }

    public String getDefaultGroovyScript() {
        return "return task.getAssigneeId().equals(\"0\")";
    }

    public String getConfigPage() {
        return "";
    }
}
