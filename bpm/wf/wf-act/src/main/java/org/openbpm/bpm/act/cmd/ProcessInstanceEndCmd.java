package org.openbpm.bpm.act.cmd;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

public class ProcessInstanceEndCmd implements Command<Void> {
    private String processInstanceId = null;

    public ProcessInstanceEndCmd(String processInstanceId2) {
        this.processInstanceId = processInstanceId2;
    }

    public Void execute(CommandContext cmdContext) {
        getParent(cmdContext.getExecutionEntityManager().findExecutionById(this.processInstanceId)).end();
        return null;
    }

    private ExecutionEntity getParent(ExecutionEntity executionEntity) {
        ExecutionEntity parentEnt = executionEntity.getParent();
        return parentEnt == null ? executionEntity : getParent(parentEnt);
    }
}
