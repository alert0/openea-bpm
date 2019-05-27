package org.openbpm.bpm.engine.action.handler.instance;

import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.act.service.ActInstanceService;
import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.constant.InstanceStatus;
import org.openbpm.bpm.api.constant.NodeType;
import org.openbpm.bpm.api.constant.OpinionStatus;
import org.openbpm.bpm.api.engine.action.cmd.InstanceActionCmd;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.engine.plugin.cmd.ExecutionCommand;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.model.inst.IBpmInstance;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.core.manager.BpmTaskManager;
import org.openbpm.bpm.core.manager.BpmTaskOpinionManager;
import org.openbpm.bpm.core.manager.BpmTaskStackManager;
import org.openbpm.bpm.core.model.BpmInstance;
import org.openbpm.bpm.core.model.BpmTask;
import org.openbpm.bpm.core.model.BpmTaskOpinion;
import org.openbpm.bpm.engine.action.cmd.DefaultInstanceActionCmd;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.openbpm.bpm.engine.action.handler.AbsActionHandler;
import org.openbpm.org.api.model.IUser;
import org.openbpm.sys.util.ContextUtil;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InstanceManualEndActionHandler extends AbsActionHandler<DefualtTaskActionCmd> {
    @Autowired
    ActInstanceService actInstanceService;
    @Autowired
    BpmTaskManager bpmTaskManager;
    @Autowired
    BpmTaskOpinionManager bpmTaskOpinionManager;
    @Autowired
    BpmTaskStackManager bpmTaskStackManager;
    @Autowired
    ExecutionCommand executionCommand;

    @Transactional
    public void execute(DefualtTaskActionCmd model) {
        BpmTask task = (BpmTask) this.bpmTaskManager.get(model.getTaskId());
        if (task == null) {
            throw new BusinessException(BpmStatusCode.TASK_NOT_FOUND);
        }
        model.setBpmTask(task);
        model.setDefId(task.getDefId());
        model.setBpmInstance((IBpmInstance) this.bpmInstanceManager.get(model.getInstanceId()));
        checkFlowIsValid(model);
        BpmContext.setActionModel(model);
        updateOpinionStatus(model);
        handleInstanceInfo(model);
        BpmContext.removeActionModel();
    }

    public ActionType getActionType() {
        return ActionType.MANUALEND;
    }

    public int getSn() {
        return 6;
    }

    public Boolean isSupport(BpmNodeDef nodeDef) {
        NodeType nodeType = nodeDef.getType();
        if (nodeType == NodeType.USERTASK || nodeType == NodeType.SIGNTASK) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    public Boolean isDefault() {
        return Boolean.valueOf(false);
    }

    public String getConfigPage() {
        return "/bpm/task/taskOpinionDialog.html";
    }

    public String getDefaultGroovyScript() {
        return "";
    }

    public String getDefaultBeforeScript() {
        return "";
    }

    private void updateOpinionStatus(DefualtTaskActionCmd actionModel) {
        BpmTaskOpinion bpmTaskOpinion = this.bpmTaskOpinionManager.getByTaskId(actionModel.getTaskId());
        if (bpmTaskOpinion != null) {
            bpmTaskOpinion.setStatus(OpinionStatus.getByActionName(actionModel.getActionName()).getKey());
            bpmTaskOpinion.setApproveTime(new Date());
            bpmTaskOpinion.setDurMs(Long.valueOf(bpmTaskOpinion.getApproveTime().getTime() - bpmTaskOpinion.getCreateTime().getTime()));
            bpmTaskOpinion.setOpinion(actionModel.getOpinion());
            IUser user = ContextUtil.getCurrentUser();
            if (user != null) {
                bpmTaskOpinion.setApprover(user.getUserId());
                bpmTaskOpinion.setApproverName(user.getFullname());
            }
            this.bpmTaskOpinionManager.update(bpmTaskOpinion);
        }
    }

    private void handleInstanceInfo(DefualtTaskActionCmd model) {
        BpmInstance instance = (BpmInstance) model.getBpmInstance();
        if (instance == null) {
            instance = (BpmInstance) this.bpmInstanceManager.get(model.getInstanceId());
            model.setBpmInstance(instance);
        }
        this.actInstanceService.deleteProcessInstance(instance.getActInstId(), model.getOpinion());
        this.bpmTaskManager.removeByInstId(instance.getId());
        this.taskIdentityLinkManager.removeByInstId(instance.getId());
        instance.setStatus(InstanceStatus.STATUS_MANUAL_END.getKey());
        instance.setEndTime(new Date());
        instance.setDuration(Long.valueOf(instance.getEndTime().getTime() - instance.getCreateTime().getTime()));
        this.bpmInstanceManager.update(instance);
        this.bpmTaskStackManager.removeByInstanceId(model.getInstanceId());
        this.executionCommand.execute(EventType.MANUAL_END, getInstCmd(model));
        if (!StringUtil.isZeroEmpty(instance.getParentInstId())) {
            for (BpmInstance inst : this.bpmInstanceManager.getByPId(instance.getParentInstId())) {
                if (InstanceStatus.STATUS_RUNNING.getKey().equals(inst.getStatus())) {
                    return;
                }
            }
            model.setInstanceId(instance.getParentInstId());
            handleInstanceInfo(model);
            model.setInstanceId(instance.getId());
        }
    }

    private InstanceActionCmd getInstCmd(DefualtTaskActionCmd model) {
        DefaultInstanceActionCmd instanceActionCmd = new DefaultInstanceActionCmd();
        instanceActionCmd.setActionName(model.getActionName());
        instanceActionCmd.setActionVariables(model.getActionVariables());
        instanceActionCmd.setBizDataMap(model.getBizDataMap());
        instanceActionCmd.setBpmDefinition(model.getBpmDefinition());
        instanceActionCmd.setBpmIdentities(model.getBpmIdentities());
        instanceActionCmd.setBpmInstance(model.getBpmInstance());
        instanceActionCmd.setBusData(model.getBusData());
        instanceActionCmd.setBusinessKey(model.getBusinessKey());
        instanceActionCmd.setDataMode(model.getDataMode());
        instanceActionCmd.setDefId(model.getDefId());
        instanceActionCmd.setDestination(model.getDestination());
        instanceActionCmd.setFormId(model.getFormId());
        return instanceActionCmd;
    }

    /* access modifiers changed from: protected */
    public void doAction(DefualtTaskActionCmd model) {
    }

    /* access modifiers changed from: protected */
    public void prepareActionDatas(DefualtTaskActionCmd data) {
    }

    /* access modifiers changed from: protected */
    public void doActionBefore(DefualtTaskActionCmd actionModel) {
    }

    /* access modifiers changed from: protected */
    public void doActionAfter(DefualtTaskActionCmd actionModel) {
    }
}
