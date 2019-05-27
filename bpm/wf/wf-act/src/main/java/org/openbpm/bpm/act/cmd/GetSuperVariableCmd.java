package org.openbpm.bpm.act.cmd;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

public class GetSuperVariableCmd implements Command<Object> {
    private String bpmnInstId = "";
    private String varName = "";

    public void setBpmnInstId(String bpmnInstId2) {
        this.bpmnInstId = bpmnInstId2;
    }

    public void setVarName(String varName2) {
        this.varName = varName2;
    }

    public GetSuperVariableCmd() {
    }

    public GetSuperVariableCmd(String bpmnInstId2, String varName2) {
        this.bpmnInstId = bpmnInstId2;
        this.varName = varName2;
    }

    public Object execute(CommandContext context) {
        ExecutionEntity execution = context.getExecutionEntityManager().findExecutionById(this.bpmnInstId);
        if (execution.getSuperExecution() == null) {
            return null;
        }
        return execution.getSuperExecution().getVariable(this.varName);
    }
}
