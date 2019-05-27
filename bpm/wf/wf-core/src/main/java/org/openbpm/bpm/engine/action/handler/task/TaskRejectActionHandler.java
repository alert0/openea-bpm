package org.openbpm.bpm.engine.action.handler.task;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.exception.WorkFlowException;
import org.openbpm.bpm.api.model.def.NodeProperties;
import org.openbpm.bpm.api.model.inst.BpmExecutionStack;
import org.openbpm.bpm.api.model.task.IBpmTask;
import org.openbpm.bpm.core.manager.BpmTaskOpinionManager;
import org.openbpm.bpm.core.manager.BpmTaskStackManager;
import org.openbpm.bpm.core.model.BpmTask;
import org.openbpm.bpm.core.model.BpmTaskOpinion;
import org.openbpm.bpm.core.model.BpmTaskStack;
import org.openbpm.bpm.core.model.TaskIdentityLink;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.openbpm.bpm.engine.model.BpmIdentity;
import org.openbpm.sys.api.model.SysIdentity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TaskRejectActionHandler extends AbstractTaskActionHandler<DefualtTaskActionCmd> {
    private static Logger log = LoggerFactory.getLogger(TaskRejectActionHandler.class);
    @Resource
    BpmTaskStackManager bpmTaskStackManager;
    @Resource
    BpmTaskOpinionManager taskOpinionManager;

    public void doActionBefore(DefualtTaskActionCmd actionModel) {
        NodeProperties nodeProperties = this.bpmProcessDefService.getBpmNodeDef(actionModel.getDefId(), actionModel.getNodeId()).getNodeProperties();
        String destinationNode = getPreDestination(actionModel, nodeProperties);
        if ("history".equals(nodeProperties.getBackUserMode())) {
            setHistoryApprover(actionModel, destinationNode);
        }
        IBpmTask task = actionModel.getBpmTask();
        if (task.getNodeId().equals(destinationNode)) {
            throw new BusinessException("目标节点不能为当前任务节点", BpmStatusCode.CANNOT_BACK_NODE);
        }
        actionModel.setDestination(destinationNode);
        log.info("任务【{}-{}】将驳回至节点{}", new Object[]{task.getName(), task.getId(), destinationNode});
    }

    private void setHistoryApprover(DefualtTaskActionCmd actionModel, String destinationNode) {
        SysIdentity identitys = null;
        for (BpmTaskOpinion opinion : this.taskOpinionManager.getByInstAndNode(actionModel.getInstanceId(), actionModel.getBpmTask().getNodeId())) {
            if (StringUtil.isNotEmpty(opinion.getApprover())) {
                identitys = new BpmIdentity(opinion.getApprover(), opinion.getApproverName(), TaskIdentityLink.RIGHT_TYPE_USER);
            }
        }
        if (identitys != null) {
            List<SysIdentity> list = new ArrayList<>();
            list.add(identitys);
            actionModel.setBpmIdentity(destinationNode, list);
        }
    }

    protected String getPreDestination(DefualtTaskActionCmd actionModel, NodeProperties nodeProperties) {
        if (StringUtil.isNotEmpty(actionModel.getDestination())) {
            return actionModel.getDestination();
        }
        if (nodeProperties != null && StringUtil.isNotEmpty(nodeProperties.getBackNode())) {
            return nodeProperties.getBackNode();
        }
        List<BpmTaskStack> stackList = this.bpmTaskStackManager.getByInstanceId(actionModel.getInstanceId());
        BpmTaskStack currentStack = null;
        int i = stackList.size() - 1;
        while (true) {
            if (i <= -1) {
                break;
            }
            BpmTaskStack stack = (BpmTaskStack) stackList.get(i);
            if (stack.getTaskId().equals(actionModel.getTaskId())) {
                currentStack = stack;
                break;
            }
            i--;
        }
        if (currentStack == null) {
            throw new BusinessException(actionModel.getTaskId() + " currentStack lost 堆栈信息异常 ");
        }
        BpmExecutionStack preStack = getPreTaskStack(stackList, currentStack.getParentId());
        if (preStack != null) {
            return preStack.getNodeId();
        }
        throw new WorkFlowException("上一任务处理记录查找失败，无法驳回！", BpmStatusCode.NO_BACK_TARGET);
    }

    BpmExecutionStack getPreTaskStack(List<BpmTaskStack> stackList, String id) {
        String parentId = id;
        for (int i = stackList.size() - 1; i > -1; i--) {
            BpmTaskStack stack = (BpmTaskStack) stackList.get(i);
            if (stack.getId().equals(parentId)) {
                parentId = stack.getParentId();
                if ("userTask".equals(stack.getNodeType())) {
                    return stack;
                }
            }
        }
        return null;
    }

    public void doActionAfter(DefualtTaskActionCmd actionModel) {
        if ("back".equals(this.bpmProcessDefService.getBpmNodeDef(actionModel.getDefId(), actionModel.getNodeId()).getNodeProperties().getBackMode())) {
            List<BpmTask> tasks = this.taskManager.getByInstIdNodeId(actionModel.getInstanceId(), actionModel.getNodeId());
            if (CollectionUtil.isEmpty(tasks)) {
                throw new WorkFlowException("任务返回节点标记失败，待标记任务查找不到", BpmStatusCode.NO_BACK_TARGET);
            }
            boolean hasUpdated = false;
            for (BpmTask task : tasks) {
                if (StringUtil.isNotEmpty(task.getActInstId()) && StringUtil.isNotEmpty(task.getTaskId())) {
                    if (hasUpdated) {
                        throw new WorkFlowException("任务返回节点标记失败，期望查找一条，但是出现多条", BpmStatusCode.NO_BACK_TARGET);
                    }
                    task.setBackNode(actionModel.getNodeId());
                    this.taskManager.update(task);
                    hasUpdated = true;
                }
            }
            if (!hasUpdated) {
                throw new WorkFlowException("任务返回节点标记失败，待标记任务查找不到", BpmStatusCode.NO_BACK_TARGET);
            }
        }
    }

    public ActionType getActionType() {
        return ActionType.REJECT;
    }

    public int getSn() {
        return 3;
    }

    public String getConfigPage() {
        return "/bpm/task/taskOpinionDialog.html";
    }
}
