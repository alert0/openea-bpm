package org.openbpm.bpm.engine.action.handler.task;

import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.act.service.ActTaskService;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.constant.NodeType;
import org.openbpm.bpm.api.engine.plugin.cmd.TaskCommand;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.model.inst.IBpmInstance;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.core.manager.BpmTaskManager;
import org.openbpm.bpm.core.model.BpmTask;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.openbpm.bpm.engine.action.handler.AbsActionHandler;
import javax.annotation.Resource;

public abstract class AbstractTaskActionHandler<T extends DefualtTaskActionCmd> extends AbsActionHandler<T> {
    @Resource
    protected ActTaskService actTaskService;
    @Resource
    protected TaskCommand taskCommand;
    @Resource
    protected BpmTaskManager taskManager;

    public void doAction(T actionModel) {
        BpmTask bpmTask = (BpmTask) actionModel.getBpmTask();
        String taskId = bpmTask.getTaskId();
        String destinationNode = bpmTask.getBackNode();
        if (StringUtil.isEmpty(destinationNode)) {
            destinationNode = actionModel.getDestination();
        }
        if (StringUtil.isEmpty(destinationNode)) {
            this.actTaskService.completeTask(taskId, actionModel.getActionVariables());
            return;
        }
        this.actTaskService.completeTask(taskId, actionModel.getActionVariables(), new String[]{destinationNode});
    }

    /* access modifiers changed from: protected */
    public void prepareActionDatas(T data) {
        if (data.getBpmTask() == null) {
            BpmTask task = (BpmTask) this.taskManager.get(data.getTaskId());
            if (task == null) {
                throw new BusinessException(BpmStatusCode.TASK_NOT_FOUND);
            }
            data.setBpmTask(task);
            data.setDefId(task.getDefId());
            data.setBpmInstance((IBpmInstance) this.bpmInstanceManager.get(task.getInstId()));
            parserBusinessData(data);
            handelFormInit(data, this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), task.getNodeId()));
        }
    }

    public Boolean isSupport(BpmNodeDef nodeDef) {
        NodeType nodeType = nodeDef.getType();
        if (nodeType == NodeType.USERTASK || nodeType == NodeType.SIGNTASK) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    protected void taskComplatePrePluginExecute(DefualtTaskActionCmd actionModel) {
        this.taskCommand.execute(EventType.TASK_PRE_COMPLETE_EVENT, actionModel);
    }

    public String getDefaultGroovyScript() {
        return "";
    }
}
