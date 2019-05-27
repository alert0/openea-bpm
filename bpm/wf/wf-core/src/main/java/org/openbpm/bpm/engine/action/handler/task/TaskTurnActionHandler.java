package org.openbpm.bpm.engine.action.handler.task;

import cn.hutool.core.bean.BeanUtil;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.id.IdUtil;
import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.constant.TaskStatus;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.core.manager.BpmTaskOpinionManager;
import org.openbpm.bpm.core.model.BpmTask;
import org.openbpm.bpm.core.model.BpmTaskOpinion;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.openbpm.sys.api.model.SysIdentity;
import org.openbpm.sys.util.ContextUtil;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class TaskTurnActionHandler extends AbstractTaskActionHandler<DefualtTaskActionCmd> {
    @Resource
    BpmTaskOpinionManager bpmTaskOpinionManager;

    public ActionType getActionType() {
        return ActionType.TURN;
    }

    public void execute(DefualtTaskActionCmd model) {
        prepareActionDatas(model);
        checkFlowIsValid(model);
        BpmTask task = (BpmTask) model.getBpmTask();
        List<SysIdentity> userSetting = model.getBpmIdentity(task.getNodeId());
        if (BeanUtil.isEmpty(userSetting)) {
            throw new BusinessException(BpmStatusCode.NO_ASSIGN_USER);
        }
        task.setAssigneeId(((SysIdentity) userSetting.get(0)).getId());
        task.setAssigneeNames(((SysIdentity) userSetting.get(0)).getName());
        task.setStatus(TaskStatus.TURN.getKey());
        this.taskManager.update(task);
        handelTaskOpinion(task, model.getOpinion());
    }

    private void handelTaskOpinion(BpmTask task, String taskOpinion) {
        BpmTaskOpinion opinion = this.bpmTaskOpinionManager.getByTaskId(task.getId());
        String opinionId = opinion.getId();
        opinion.setId(IdUtil.getSuid());
        this.bpmTaskOpinionManager.create(opinion);
        opinion.setId(opinionId);
        opinion.setTaskId("-");
        opinion.setOpinion(taskOpinion);
        opinion.setStatus(ActionType.TURN.getKey());
        opinion.setApproveTime(new Date());
        opinion.setApprover(ContextUtil.getCurrentUserId());
        opinion.setApproverName(ContextUtil.getCurrentUser().getFullname());
        opinion.setDurMs(Long.valueOf(opinion.getApproveTime().getTime() - opinion.getCreateTime().getTime()));
        this.bpmTaskOpinionManager.update(opinion);
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
        return Boolean.valueOf(true);
    }

    public String getConfigPage() {
        return "/bpm/task/taskTrunActionDialog.html";
    }
}
