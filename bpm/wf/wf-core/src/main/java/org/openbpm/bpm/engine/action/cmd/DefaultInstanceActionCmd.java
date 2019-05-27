package org.openbpm.bpm.engine.action.cmd;

import org.openbpm.bpm.api.engine.action.cmd.BaseActionCmd;
import org.openbpm.bpm.api.engine.action.cmd.FlowRequestParam;
import org.openbpm.bpm.api.engine.action.cmd.InstanceActionCmd;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.exception.WorkFlowException;
import java.util.Map;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

public class DefaultInstanceActionCmd extends BaseActionCmd implements InstanceActionCmd {
    protected ExecutionEntity executionEntity;

    public DefaultInstanceActionCmd(FlowRequestParam flowParam) {
        super(flowParam);
    }

    public DefaultInstanceActionCmd() {
    }

    public String getFlowKey() {
        return getBpmDefinition().getKey();
    }

    public String getSubject() {
        return getBpmInstance().getSubject();
    }

    public ExecutionEntity getExecutionEntity() {
        return this.executionEntity;
    }

    public void setExecutionEntity(ExecutionEntity executionEntity2) {
        this.executionEntity = executionEntity2;
    }

    public Object getVariable(String variableName) {
        if (this.executionEntity != null) {
            return this.executionEntity.getVariable(variableName);
        }
        throw new WorkFlowException(BpmStatusCode.VARIABLES_NO_SYNC);
    }

    public boolean hasVariable(String variableName) {
        if (this.executionEntity != null) {
            return this.executionEntity.hasVariable(variableName);
        }
        throw new WorkFlowException(BpmStatusCode.VARIABLES_NO_SYNC);
    }

    public void removeVariable(String variableName) {
        if (this.executionEntity == null) {
            throw new WorkFlowException(BpmStatusCode.VARIABLES_NO_SYNC);
        }
        this.executionEntity.removeVariable(variableName);
    }

    public void addVariable(String name, Object value) {
        if (this.executionEntity == null) {
            throw new WorkFlowException(BpmStatusCode.VARIABLES_NO_SYNC);
        }
        this.executionEntity.setVariable(name, value);
    }

    public Map<String, Object> getVariables() {
        return this.executionEntity.getVariables();
    }

    public void initSpecialParam(FlowRequestParam flowParam) {
    }

    public String getNodeId() {
        if (this.executionEntity == null) {
            return null;
        }
        return this.executionEntity.getActivityId();
    }
}
