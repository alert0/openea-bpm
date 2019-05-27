package org.openbpm.bpm.engine.plugin.session.impl;

import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.engine.action.cmd.BaseActionCmd;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.model.inst.IBpmInstance;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.openbpm.bpm.engine.plugin.session.BpmExecutionPluginSession;
import org.openbpm.bus.api.model.IBusinessData;
import org.openbpm.sys.util.ContextUtil;
import java.util.HashMap;
import java.util.Map;
import org.activiti.engine.delegate.VariableScope;

public class DefaultBpmExecutionPluginSession extends HashMap<String, Object> implements BpmExecutionPluginSession {
    private static final long serialVersionUID = 4225343560381914372L;
    private Map<String, IBusinessData> boDatas;
    private IBpmInstance bpmInstance;
    private EventType eventType;
    private VariableScope variableScope;

    public DefaultBpmExecutionPluginSession() {
        BaseActionCmd cmd = (BaseActionCmd) BpmContext.submitActionModel();
        ActionType actionType = cmd.getActionType();
        put("submitActionDesc", actionType.getName());
        put("submitActionName", actionType.getKey());
        if (cmd instanceof DefualtTaskActionCmd) {
            DefualtTaskActionCmd taskCmd = (DefualtTaskActionCmd) cmd;
            put("isTask", Boolean.valueOf(true));
            put("submitOpinion", taskCmd.getOpinion());
            put("currentUser", ContextUtil.getCurrentUser());
            put("submitTaskname", taskCmd.getBpmTask().getName());
        }
    }

    public Map<String, IBusinessData> getBoDatas() {
        return this.boDatas;
    }

    public void setBoDatas(Map<String, IBusinessData> boDatas2) {
        putAll(boDatas2);
        this.boDatas = boDatas2;
    }

    public IBpmInstance getBpmInstance() {
        return this.bpmInstance;
    }

    public void setBpmInstance(IBpmInstance bpmInstance2) {
        put("bpmInstance", bpmInstance2);
        this.bpmInstance = bpmInstance2;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public void setEventType(EventType eventType2) {
        put("eventType", eventType2.getKey());
        this.eventType = eventType2;
    }

    public VariableScope getVariableScope() {
        return this.variableScope;
    }

    public void setVariableScope(VariableScope variableScope2) {
        put("variableScope", variableScope2);
        this.variableScope = variableScope2;
    }
}
