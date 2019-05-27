package org.activiti.engine.impl.el;

import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.bpm.api.engine.action.cmd.BaseActionCmd;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.sys.api.groovy.IGroovyScriptEngine;
import java.util.Map;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.Condition;

public class GroovyCondition implements Condition {
    private static final long serialVersionUID = -5577703954744892854L;
    private String script = "";

    public GroovyCondition(String condition) {
        this.script = condition;
    }

    public boolean evaluate(String arg0, DelegateExecution execution) {
        Map<String, Object> maps = execution.getVariables();
        maps.put("variableScope", execution);
        maps.putAll(BpmContext.getActionModel().getBizDataMap());
        BaseActionCmd submitAction = (BaseActionCmd) BpmContext.submitActionModel();
        maps.put("submitActionName", submitAction.getActionType().getKey());
        maps.put("bpmInstance", submitAction.getBpmInstance());
        try {
            return ((IGroovyScriptEngine) AppUtil.getBean(IGroovyScriptEngine.class)).executeBoolean(this.script, maps);
        } catch (Exception e) {
            e.printStackTrace();
            StringBuilder message = new StringBuilder("条件脚本解析异常！请联系管理员。");
            message.append("\n节点：" + execution.getCurrentActivityName() + "——" + execution.getCurrentActivityId());
            message.append("\n脚本：" + this.script);
            message.append("\n异常：" + e.getMessage());
            message.append("\n\n流程变量：" + maps.toString());
            throw new BusinessException(message.toString(), BpmStatusCode.GATEWAY_ERROR, e);
        }
    }
}
