package org.openbpm.bpm.engine.action.handler.instance;

import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.constant.InstanceStatus;
import org.openbpm.bpm.api.constant.NodeType;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.core.model.BpmInstance;
import org.openbpm.bpm.engine.action.cmd.DefaultInstanceActionCmd;
import org.openbpm.bpm.engine.action.handler.AbsActionHandler;
import org.springframework.stereotype.Component;

@Component
public class InstanceSaveActionHandler extends AbsActionHandler<DefaultInstanceActionCmd> {
    /* access modifiers changed from: protected */
    public void doAction(DefaultInstanceActionCmd model) {
        model.getBpmInstance().setStatus(InstanceStatus.STATUS_DRAFT.getKey());
    }

    /* access modifiers changed from: protected */
    public void doActionBefore(DefaultInstanceActionCmd actionModel) {
    }

    /* access modifiers changed from: protected */
    public void prepareActionDatas(DefaultInstanceActionCmd data) {
        data.setBpmDefinition(this.bpmProcessDefService.getDefinitionById(data.getDefId()));
        getInstance(data);
        parserBusinessData(data);
        handelFormInit(data, this.bpmProcessDefService.getStartEvent(data.getDefId()));
    }

    /* access modifiers changed from: protected */
    public void doActionAfter(DefaultInstanceActionCmd actionModel) {
        persistenceInstance(actionModel);
    }

    protected void persistenceInstance(DefaultInstanceActionCmd actionModel) {
        BpmInstance instance = (BpmInstance) actionModel.getBpmInstance();
        if (instance.hasCreate().booleanValue()) {
            this.bpmInstanceManager.update(instance);
        } else {
            this.bpmInstanceManager.create(instance);
        }
    }

    protected void getInstance(DefaultInstanceActionCmd intanceCmdData) {
        String instId = intanceCmdData.getInstanceId();
        BpmInstance instance = null;
        if (StringUtil.isNotEmpty(instId)) {
            instance = (BpmInstance) this.bpmInstanceManager.get(instId);
            if (StringUtil.isNotEmpty(instance.getActInstId())) {
                throw new BusinessException("草稿已经启动，请勿多次启动该草稿！");
            }
        }
        if (instance == null) {
            instance = this.bpmInstanceManager.genInstanceByDefinition(intanceCmdData.getBpmDefinition());
        }
        intanceCmdData.setBpmInstance(instance);
    }

    public ActionType getActionType() {
        return ActionType.DRAFT;
    }

    public int getSn() {
        return 2;
    }

    public Boolean isSupport(BpmNodeDef nodeDef) {
        if (nodeDef.getType() == NodeType.START) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    public String getConfigPage() {
        return null;
    }

    public String getDefaultGroovyScript() {
        return "";
    }
}
