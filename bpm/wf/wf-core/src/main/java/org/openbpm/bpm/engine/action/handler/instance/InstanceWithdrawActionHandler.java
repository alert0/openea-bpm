package org.openbpm.bpm.engine.action.handler.instance;

import org.openbpm.base.core.util.JsonUtil;
import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.core.manager.BpmTaskManager;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.openbpm.bpm.engine.action.handler.AbsActionHandler;
import org.openbpm.sys.api.jms.producer.JmsProducer;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class InstanceWithdrawActionHandler extends AbsActionHandler<DefualtTaskActionCmd> {
    @Resource
    JmsProducer jmsProducer;
    @Resource
    BpmTaskManager taskMananger;

    public void execute(DefualtTaskActionCmd model) {
        String opinion = model.getOpinion();
        String string = JsonUtil.getString(model.getExtendConf(), "withdrawNodeId");
        String string2 = JsonUtil.getString(model.getExtendConf(), "msgType");
    }

    public ActionType getActionType() {
        return ActionType.WITHDRAW;
    }

    public int getSn() {
        return 7;
    }

    public Boolean isSupport(BpmNodeDef nodeDef) {
        return Boolean.valueOf(false);
    }

    public Boolean isDefault() {
        return Boolean.valueOf(false);
    }

    public String getConfigPage() {
        return null;
    }

    public String getDefaultGroovyScript() {
        return null;
    }

    public String getDefaultBeforeScript() {
        return null;
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
