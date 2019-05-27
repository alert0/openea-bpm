package org.openbpm.bpm.engine.action.handler.instance;

import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.act.service.ActInstanceService;
import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.constant.NodeType;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.bpm.core.manager.BpmInstanceManager;
import org.openbpm.bpm.core.model.BpmInstance;
import org.openbpm.bpm.engine.action.cmd.DefaultInstanceActionCmd;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class InstanceStartActionHandler extends InstanceSaveActionHandler {
    @Resource
    ActInstanceService actInstanceService;
    @Resource
    BpmInstanceManager bpmInstanceManager;
    @Resource
    BpmProcessDefService bpmProcessDefService;

    public void doAction(DefaultInstanceActionCmd startActionModel) {
        String actInstId;
        String destination = startActionModel.getDestination();
        BpmInstance instance = (BpmInstance) startActionModel.getBpmInstance();
        if (StringUtil.isEmpty(destination)) {
            actInstId = this.actInstanceService.startProcessInstance(instance.getActDefId(), instance.getBizKey(), startActionModel.getActionVariables());
        } else {
            actInstId = this.actInstanceService.startProcessInstance(instance.getActDefId(), instance.getBizKey(), startActionModel.getActionVariables(), new String[]{destination});
        }
        instance.setActInstId(actInstId);
        persistenceInstance(startActionModel);
    }

    /* access modifiers changed from: protected */
    public void doActionAfter(DefaultInstanceActionCmd actionModel) {
    }

    public int getSn() {
        return 1;
    }

    public ActionType getActionType() {
        return ActionType.START;
    }

    public Boolean isSupport(BpmNodeDef nodeDef) {
        return Boolean.valueOf(nodeDef.getType() == NodeType.START);
    }
}
