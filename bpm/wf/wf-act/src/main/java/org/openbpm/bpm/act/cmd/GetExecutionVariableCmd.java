package org.openbpm.bpm.act.cmd;

import java.io.Serializable;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

public class GetExecutionVariableCmd implements Serializable, Command<Object> {
    private static final long serialVersionUID = 1;
    protected String executionId;
    protected boolean isLocal;
    protected String variableName;

    public GetExecutionVariableCmd(String executionId2, String variableName2, boolean isLocal2) {
        this.executionId = executionId2;
        this.variableName = variableName2;
        this.isLocal = isLocal2;
    }

    public Object execute(CommandContext commandContext) {
        if (this.executionId == null) {
            throw new ActivitiIllegalArgumentException("executionId is null");
        } else if (this.variableName == null) {
            throw new ActivitiIllegalArgumentException("variableName is null");
        } else {
            ExecutionEntity execution = commandContext.getExecutionEntityManager().findExecutionById(this.executionId);
            if (execution == null) {
                return null;
            }
            if (this.isLocal) {
                return execution.getVariableLocal(this.variableName);
            }
            return execution.getVariable(this.variableName);
        }
    }
}
