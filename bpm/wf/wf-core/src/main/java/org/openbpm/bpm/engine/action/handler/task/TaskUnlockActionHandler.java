package org.openbpm.bpm.engine.action.handler.task;

import org.openbpm.base.api.exception.BusinessMessage;
import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.constant.TaskStatus;
import org.openbpm.bpm.core.manager.BpmTaskManager;
import org.openbpm.bpm.core.model.BpmTask;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class TaskUnlockActionHandler extends AbstractTaskActionHandler<DefualtTaskActionCmd> {
    @Resource
    BpmTaskManager bpmTaskMananger;

    public ActionType getActionType() {
        return ActionType.UNLOCK;
    }

    public void execute(DefualtTaskActionCmd model) {
        prepareActionDatas(model);
        checkFlowIsValid(model);
        BpmTask task = (BpmTask) model.getBpmTask();
        if (!task.getStatus().equals(TaskStatus.LOCK.getKey())) {
            throw new BusinessMessage("该任务并非锁定状态,或已经被解锁，解锁失败");
        }
        this.bpmTaskMananger.unLockTask(task.getId());
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
        return "return task.getStatus().equals(\"LOCK\");";
    }

    public String getConfigPage() {
        return "";
    }
}
