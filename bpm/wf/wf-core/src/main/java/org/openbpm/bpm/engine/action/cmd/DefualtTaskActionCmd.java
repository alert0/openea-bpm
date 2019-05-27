package org.openbpm.bpm.engine.action.cmd;

import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.engine.action.cmd.BaseActionCmd;
import org.openbpm.bpm.api.engine.action.cmd.FlowRequestParam;
import org.openbpm.bpm.api.engine.action.cmd.TaskActionCmd;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.exception.WorkFlowException;
import org.openbpm.bpm.api.model.task.IBpmTask;
import org.openbpm.bpm.engine.action.handler.AbsActionHandler;
import org.openbpm.bpm.engine.constant.TaskSkipType;
import java.util.Map;
import org.activiti.engine.delegate.DelegateTask;

public class DefualtTaskActionCmd extends BaseActionCmd implements TaskActionCmd {
    private IBpmTask bpmTask;
    private DelegateTask delagateTask;
    private TaskSkipType hasSkipThisTask = TaskSkipType.NO_SKIP;
    private String taskId;

    public DefualtTaskActionCmd() {
    }

    public DefualtTaskActionCmd(FlowRequestParam flowParam) {
        super(flowParam);
    }

    public String getTaskId() {
        if (this.bpmTask != null) {
            return this.bpmTask.getId();
        }
        return this.taskId;
    }

    public void setTaskId(String taskId2) {
        this.taskId = taskId2;
    }

    public void initSpecialParam(FlowRequestParam flowParam) {
        String taskId2 = flowParam.getTaskId();
        if (StringUtil.isEmpty(taskId2)) {
            throw new BusinessException("taskId 不能为空", BpmStatusCode.TASK_NOT_FOUND);
        }
        setTaskId(taskId2);
        setDestination(flowParam.getDestination());
    }

    public IBpmTask getBpmTask() {
        return this.bpmTask;
    }

    public void setBpmTask(IBpmTask task) {
        this.bpmTask = task;
    }

    public DelegateTask getDelagateTask() {
        if (this.delagateTask == null) {
        }
        return this.delagateTask;
    }

    public void setDelagateTask(DelegateTask task) {
        this.delagateTask = task;
    }

    public String getNodeId() {
        return this.bpmTask.getNodeId();
    }

    public void addVariable(String variableName, Object value) {
        if (this.delagateTask == null) {
            throw new WorkFlowException(BpmStatusCode.VARIABLES_NO_SYNC);
        }
        this.delagateTask.setVariable(variableName, value);
    }

    public Object getVariable(String variableName) {
        if (this.delagateTask != null) {
            return this.delagateTask.getVariable(variableName);
        }
        throw new WorkFlowException(BpmStatusCode.VARIABLES_NO_SYNC);
    }

    public boolean hasVariable(String variableName) {
        if (this.delagateTask != null) {
            return this.delagateTask.hasVariable(variableName);
        }
        throw new WorkFlowException(BpmStatusCode.VARIABLES_NO_SYNC);
    }

    public void removeVariable(String variableName) {
        if (this.delagateTask == null) {
            throw new WorkFlowException(BpmStatusCode.VARIABLES_NO_SYNC);
        }
    }

    public Map<String, Object> getVariables() {
        return this.delagateTask.getVariables();
    }

    public synchronized String executeSkipTask() {
        AbsActionHandler handler;
        if (this.hasExecuted) {
            throw new BusinessException("action cmd caonot be invoked twice", BpmStatusCode.PARAM_ILLEGAL);
        }
        this.hasExecuted = true;
        ActionType actonType = ActionType.fromKey(getActionName());
        handler = (AbsActionHandler) AppUtil.getBean(actonType.getBeanId());
        if (handler == null) {
            throw new BusinessException("action beanId cannot be found :" + actonType.getName(), BpmStatusCode.NO_TASK_ACTION);
        }
        handler.skipTaskExecute(this);
        return handler.getActionType().getName();
    }

    public TaskSkipType isHasSkipThisTask() {
        return this.hasSkipThisTask;
    }

    public void setHasSkipThisTask(TaskSkipType isSkip) {
        this.hasSkipThisTask = isSkip;
    }
}
