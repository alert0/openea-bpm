package org.openbpm.bpm.engine.listener;

import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.id.IdUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.act.listener.ActEventListener;
import org.openbpm.bpm.api.engine.action.cmd.BaseActionCmd;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.model.inst.BpmExecutionStack;
import org.openbpm.bpm.api.model.inst.IBpmInstance;
import org.openbpm.bpm.core.manager.BpmDefinitionManager;
import org.openbpm.bpm.core.manager.BpmInstanceManager;
import org.openbpm.bpm.core.manager.BpmTaskStackManager;
import org.openbpm.bpm.core.model.BpmDefinition;
import org.openbpm.bpm.core.model.BpmInstance;
import org.openbpm.bpm.core.model.BpmTaskStack;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import java.util.Date;
import javax.annotation.Resource;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.impl.ActivitiActivityEventImpl;
import org.springframework.stereotype.Component;

@Component
public class ActivityComplatedListener implements ActEventListener {
    @Resource
    private BpmDefinitionManager bpmDefinitionManager;
    @Resource
    private BpmInstanceManager bpmInstanceMananger;
    @Resource
    private BpmTaskStackManager bpmTaskStackManager;

    public void notify(ActivitiEvent event) {
        if (event instanceof ActivitiActivityEventImpl) {
            ActivitiActivityEventImpl activitEvent = (ActivitiActivityEventImpl) event;
            if (activitEvent.getActivityType().equals("callActivity")) {
                prepareSuperInstanceActionCmd(activitEvent);
            }
            if (activitEvent.getActivityType().equals("startEvent") || activitEvent.getActivityType().equals("exclusiveGateway") || activitEvent.getActivityType().equals("parallelGateway")) {
                createCommonExecutionStack(activitEvent);
            }
        }
    }

    private void createCommonExecutionStack(ActivitiActivityEventImpl event) {
        BaseActionCmd actionCmd = (BaseActionCmd) BpmContext.getActionModel();
        BpmExecutionStack taskStack = actionCmd.getExecutionStack();
        BpmTaskStack exectionStack = new BpmTaskStack();
        exectionStack.setId(IdUtil.getSuid());
        exectionStack.setNodeId(event.getActivityId());
        exectionStack.setNodeName(event.getActivityName());
        exectionStack.setTaskId(event.getExecutionId());
        exectionStack.setStartTime(new Date());
        exectionStack.setEndTime(new Date());
        exectionStack.setInstId(actionCmd.getInstanceId());
        exectionStack.setNodeType(event.getActivityType());
        exectionStack.setActionName(BpmContext.getActionModel());
        if (taskStack == null) {
            exectionStack.setParentId("0");
        } else {
            exectionStack.setParentId(taskStack.getId());
        }
        this.bpmTaskStackManager.create(exectionStack);
        actionCmd.setExecutionStack(exectionStack);
    }

    private void prepareSuperInstanceActionCmd(ActivitiActivityEventImpl activitEvent) {
        IBpmInstance childInstance = BpmContext.getActionModel().getBpmInstance();
        if (StringUtil.isZeroEmpty(childInstance.getParentInstId())) {
            throw new BusinessException("子流程提供的线程变量中，流程实例信息异常！", BpmStatusCode.ACTIONCMD_ERROR);
        }
        BpmInstance bpmInstance = (BpmInstance) this.bpmInstanceMananger.get(childInstance.getParentInstId());
        if (!bpmInstance.getActInstId().equals(activitEvent.getProcessInstanceId())) {
            throw new BusinessException("子流程提供的父流程实例，与外部子流程ACTVITI actInstanceID 不一致！", BpmStatusCode.ACTIONCMD_ERROR);
        }
        BpmTaskStack bpmTaskStack = updateExcutionStack(activitEvent.getExecutionId());
        BpmDefinition bpmDefinition = (BpmDefinition) this.bpmDefinitionManager.get(bpmInstance.getDefId());
        DefualtTaskActionCmd callActiviti = new DefualtTaskActionCmd();
        callActiviti.setBpmDefinition(bpmDefinition);
        callActiviti.setBpmInstance(bpmInstance);
        callActiviti.setExecutionStack(bpmTaskStack);
        BpmContext.setActionModel(callActiviti);
    }

    private BpmTaskStack updateExcutionStack(String executionId) {
        BpmTaskStack bpmTaskStack = this.bpmTaskStackManager.getByTaskId(executionId);
        bpmTaskStack.setEndTime(new Date());
        bpmTaskStack.setActionName(BpmContext.getActionModel());
        this.bpmTaskStackManager.update(bpmTaskStack);
        return bpmTaskStack;
    }
}
