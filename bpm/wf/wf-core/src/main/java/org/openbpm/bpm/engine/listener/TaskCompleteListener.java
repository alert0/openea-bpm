package org.openbpm.bpm.engine.listener;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.constant.InstanceStatus;
import org.openbpm.bpm.api.constant.OpinionStatus;
import org.openbpm.bpm.api.constant.ScriptType;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.core.manager.BpmInstanceManager;
import org.openbpm.bpm.core.manager.BpmTaskManager;
import org.openbpm.bpm.core.manager.BpmTaskOpinionManager;
import org.openbpm.bpm.core.manager.BpmTaskStackManager;
import org.openbpm.bpm.core.manager.TaskIdentityLinkManager;
import org.openbpm.bpm.core.model.BpmInstance;
import org.openbpm.bpm.core.model.BpmTaskOpinion;
import org.openbpm.bpm.core.model.BpmTaskStack;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.openbpm.org.api.model.IUser;
import org.openbpm.sys.util.ContextUtil;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskCompleteListener extends AbstractTaskListener<DefualtTaskActionCmd> {
    private static final long serialVersionUID = 6844821899585103714L;
    @Resource
    BpmInstanceManager bpmInstanceManager;
    @Resource
    BpmTaskManager bpmTaskManager;
    @Resource
    BpmTaskOpinionManager bpmTaskOpinionManager;
    @Resource
    BpmTaskStackManager bpmTaskStackManager;
    @Resource
    TaskIdentityLinkManager taskIdentityLinkManager;

    public EventType getBeforeTriggerEventType() {
        return EventType.TASK_COMPLETE_EVENT;
    }

    public EventType getAfterTriggerEventType() {
        return EventType.TASK_POST_COMPLETE_EVENT;
    }

    public void beforePluginExecute(DefualtTaskActionCmd taskActionModel) {
        this.LOG.debug("任务【{}】执行完成事件 - TaskID: {}", taskActionModel.getBpmTask().getName(), taskActionModel.getBpmTask().getId());
        Map<String, Object> actionVariables = taskActionModel.getActionVariables();
        if (!CollectionUtil.isEmpty(actionVariables)) {
            for (String key : actionVariables.keySet()) {
                taskActionModel.addVariable(key, actionVariables.get(key));
            }
            this.LOG.debug("设置流程变量【{}】", actionVariables.keySet().toString());
        }
    }

    public void triggerExecute(DefualtTaskActionCmd taskActionModel) {
        DefualtTaskActionCmd complateModel = taskActionModel;
        this.LOG.trace("执行任务完成动作=====》更新任务意见状态");
        updateOpinionStatus(complateModel);
        this.LOG.trace("执行任务完成动作=====》更新任务堆栈记录");
        updateExcutionStack(complateModel);
        this.LOG.trace("执行任务完成动作=====》删除任务相关信息 - 任务、任务后续人");
        delTaskRelated(complateModel);
    }

    public void afterPluginExecute(DefualtTaskActionCmd taskActionModel) {
    }

    protected ScriptType getScriptType() {
        return ScriptType.COMPLETE;
    }

    public DefualtTaskActionCmd getActionModel(TaskEntity taskEntity) {
        DefualtTaskActionCmd model = (DefualtTaskActionCmd) BpmContext.getActionModel();
        model.setDelagateTask(taskEntity);
        return model;
    }

    private void updateOpinionStatus(DefualtTaskActionCmd taskActionModel) {
        InstanceStatus flowStatus = InstanceStatus.getByActionName(taskActionModel.getActionName());
        BpmInstance instance = (BpmInstance) taskActionModel.getBpmInstance();
        if (!flowStatus.getKey().equals(instance.getStatus())) {
            instance.setStatus(flowStatus.getKey());
            this.bpmInstanceManager.update(instance);
        }
        BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(taskActionModel.getTaskId());
        if (bpmTaskOpinion != null) {
            bpmTaskOpinion.setStatus(OpinionStatus.getByActionName(taskActionModel.getActionName()).getKey());
            bpmTaskOpinion.setApproveTime(new Date());
            bpmTaskOpinion.setDurMs(Long.valueOf(bpmTaskOpinion.getApproveTime().getTime() - bpmTaskOpinion.getCreateTime().getTime()));
            bpmTaskOpinion.setOpinion(taskActionModel.getOpinion());
            IUser user = ContextUtil.getCurrentUser();
            if (user != null) {
                bpmTaskOpinion.setApprover(user.getUserId());
                bpmTaskOpinion.setApproverName(user.getFullname());
            }
            this.bpmTaskOpinionManager.update(bpmTaskOpinion);
        }
    }

    private void updateExcutionStack(DefualtTaskActionCmd taskActionModel) {
        BpmTaskStack bpmTaskStack = this.bpmTaskStackManager.getByTaskId(taskActionModel.getTaskId());
        bpmTaskStack.setEndTime(new Date());
        bpmTaskStack.setActionName(BpmContext.getActionModel());
        this.bpmTaskStackManager.update(bpmTaskStack);
        taskActionModel.setExecutionStack(bpmTaskStack);
    }

    private void delTaskRelated(DefualtTaskActionCmd taskActionModel) {
        this.taskIdentityLinkManager.removeByTaskId(taskActionModel.getTaskId());
        this.bpmTaskManager.remove(taskActionModel.getTaskId());
    }
}
