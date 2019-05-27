package org.openbpm.bpm.engine.listener;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.constant.NodeType;
import org.openbpm.bpm.api.constant.ScriptType;
import org.openbpm.bpm.api.constant.TaskStatus;
import org.openbpm.bpm.api.constant.TaskType;
import org.openbpm.bpm.api.engine.action.cmd.BaseActionCmd;
import org.openbpm.bpm.api.engine.action.cmd.TaskActionCmd;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.exception.WorkFlowException;
import org.openbpm.bpm.api.model.inst.IBpmInstance;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.api.model.task.IBpmTask;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.bpm.core.manager.BpmTaskManager;
import org.openbpm.bpm.core.manager.BpmTaskOpinionManager;
import org.openbpm.bpm.core.manager.BpmTaskStackManager;
import org.openbpm.bpm.core.manager.TaskIdentityLinkManager;
import org.openbpm.bpm.core.model.BpmTask;
import org.openbpm.bpm.core.model.TaskIdentityLink;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.openbpm.sys.api.model.SysIdentity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskCreateListener extends AbstractTaskListener<DefualtTaskActionCmd> {
    private static final long serialVersionUID = -7836822392037648008L;
    @Resource
    private BpmTaskStackManager bpmExecutionStackManager;
    @Resource
    private BpmProcessDefService bpmProcessDefService;
    @Resource
    private BpmTaskManager bpmTaskManager;
    @Resource
    private BpmTaskOpinionManager bpmTaskOpinionManager;
    @Resource
    private TaskIdentityLinkManager taskIdentityLinkManager;


    public EventType getBeforeTriggerEventType() {
        return EventType.TASK_CREATE_EVENT;
    }

    public EventType getAfterTriggerEventType() {
        return EventType.TASK_POST_CREATE_EVENT;
    }

    public void beforePluginExecute(DefualtTaskActionCmd taskActionModel) {
        this.LOG.debug("任务【{}】执行创建过程 - taskID: {}", taskActionModel.getBpmTask().getName(), taskActionModel.getBpmTask().getId());
    }

    public void triggerExecute(DefualtTaskActionCmd taskActionModel) {
        IBpmTask task = taskActionModel.getBpmTask();
        assignUser(taskActionModel);
        this.bpmTaskManager.create((BpmTask) task);
        this.bpmTaskOpinionManager.createOpinionByTask(taskActionModel);
        taskActionModel.setExecutionStack(this.bpmExecutionStackManager.createStackByTask(task, taskActionModel.getExecutionStack()));
    }

    public void afterPluginExecute(DefualtTaskActionCmd taskActionModel) {
    }

    protected ScriptType getScriptType() {
        return ScriptType.CREATE;
    }

    private void assignUser(TaskActionCmd taskActionModel) {
        IBpmTask bpmTask = taskActionModel.getBpmTask();
        List<SysIdentity> identityList = taskActionModel.getBpmIdentity(bpmTask.getNodeId());
        if (!this.bpmProcessDefService.getBpmNodeDef(bpmTask.getDefId(), bpmTask.getNodeId()).getNodeProperties().isAllowExecutorEmpty() && CollectionUtil.isEmpty(identityList)) {
            throw new WorkFlowException(bpmTask.getNodeId() + "任务候选人为空", BpmStatusCode.NO_ASSIGN_USER);
        } else if (!CollectionUtil.isEmpty(identityList)) {
            SysIdentity firstIdentity = (SysIdentity) identityList.get(0);
            if (identityList.size() != 1 || !firstIdentity.getType().equals(TaskIdentityLink.RIGHT_TYPE_USER)) {
                bpmTask.setAssigneeId("0");
                List<String> names = new ArrayList<>();
                for (SysIdentity identity : identityList) {
                    names.add(identity.getName());
                }
                bpmTask.setAssigneeNames(StringUtil.join(names));
            } else {
                bpmTask.setAssigneeId(firstIdentity.getId());
                bpmTask.setAssigneeNames(firstIdentity.getName());
            }
            this.taskIdentityLinkManager.createIdentityLink(bpmTask, identityList);
        }
    }

    public DefualtTaskActionCmd getActionModel(TaskEntity taskEntity) {
        BaseActionCmd model = (BaseActionCmd) BpmContext.getActionModel();
        if (!taskEntity.getProcessInstanceId().equals(model.getBpmInstance().getActInstId())) {
            throw new BusinessException("数据异常，actioncdm 与  TaskEntity 数据不一致，请检查！");
        }
        BpmTask task = genByActTask(taskEntity, model.getBpmInstance());
        DefualtTaskActionCmd createModel = new DefualtTaskActionCmd();
        createModel.setBpmInstance(model.getBpmInstance());
        createModel.setBpmDefinition(model.getBpmDefinition());
        createModel.setBizDataMap(model.getBizDataMap());
        createModel.setBpmIdentities(model.getBpmIdentities());
        createModel.setBusinessKey(model.getBusinessKey());
        createModel.setActionName(ActionType.CREATE.getKey());
        createModel.setBpmTask(task);
        createModel.setDelagateTask(taskEntity);
        createModel.setExecutionStack(model.getExecutionStack());
        BpmContext.setActionModel((org.openbpm.bpm.api.engine.action.cmd.ActionCmd) createModel);
        return createModel;
    }

    private BpmTask genByActTask(TaskEntity taskEntity, IBpmInstance iBpmInstance) {
        BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(iBpmInstance.getDefId(), taskEntity.getTaskDefinitionKey());
        int isSupportMobile = 1;
        if (nodeDef.getMobileForm() != null && nodeDef.getMobileForm().isFormEmpty()) {
            isSupportMobile = 0;
        }
        BpmTask task = new BpmTask();
        task.setActExecutionId(taskEntity.getExecutionId());
        task.setActInstId(taskEntity.getProcessInstanceId());
        task.setDefId(iBpmInstance.getDefId());
        task.setId(taskEntity.getId());
        task.setInstId(iBpmInstance.getId());
        task.setName(taskEntity.getName());
        task.setNodeId(taskEntity.getTaskDefinitionKey());
        task.setParentId("0");
        task.setPriority(Integer.valueOf(taskEntity.getPriority()));
        task.setStatus(TaskType.NORMAL.getKey());
        task.setTaskType(getTaskTypeByNodeType(nodeDef.getType()));
        task.setSubject(iBpmInstance.getSubject());
        task.setSupportMobile(Integer.valueOf(isSupportMobile));
        task.setStatus(TaskStatus.NORMAL.getKey());
        task.setTaskId(taskEntity.getId());
        task.setTypeId(iBpmInstance.getTypeId());
        return task;
    }

    private String getTaskTypeByNodeType(NodeType type) {
        switch (type) {
            case SIGNTASK:
                return TaskType.SIGN.getKey();
            case SUBPROCESS:
                return TaskType.SUBFLOW.getKey();
            case USERTASK:
                return TaskType.NORMAL.getKey();
            default:
                return TaskType.NORMAL.getKey();
        }
    }
}
