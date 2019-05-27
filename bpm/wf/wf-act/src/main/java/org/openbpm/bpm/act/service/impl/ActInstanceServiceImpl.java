package org.openbpm.bpm.act.service.impl;

import org.openbpm.bpm.act.cmd.GetSuperVariableCmd;
import org.openbpm.bpm.act.cmd.ProcessInstanceEndCmd;
import org.openbpm.bpm.act.service.ActInstanceService;
import org.openbpm.bpm.act.util.ActivitiUtil;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.sys.util.ContextUtil;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

@Service
public class ActInstanceServiceImpl implements ActInstanceService {
    @Resource
    BpmProcessDefService bpmProcessDefService;
    @Resource
    ProcessEngine processEngine;
    @Resource
    RuntimeService runtimeService;

    public String startProcessInstance(String actDefId, String businessKey, Map<String, Object> variables) {
        try {
            Authentication.setAuthenticatedUserId(ContextUtil.getCurrentUser().getUserId());
            String id = this.runtimeService.startProcessInstanceById(actDefId, businessKey, variables).getId();
            Authentication.setAuthenticatedUserId(null);
            return id;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } catch (Throwable th) {
            Authentication.setAuthenticatedUserId(null);
            throw th;
        }
    }

    public String startProcessInstance(String actDefId, String businessKey, Map<String, Object> variables, String[] aryDestination) {
        Map<String, Object> activityMap = ActivitiUtil.getOutTrans(actDefId, this.bpmProcessDefService.getStartEvent(this.bpmProcessDefService.getDefinitionByActDefId(actDefId).getId()).getNodeId(), aryDestination);
        String str = "";
        try {
            String actInstId = startProcessInstance(actDefId, businessKey, variables);
            ActivitiUtil.updateOutTrans(activityMap);
            return actInstId;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } catch (Throwable th) {
            ActivitiUtil.updateOutTrans(activityMap);
            throw th;
        }
    }

    public Map<String, Object> getVariables(String processInstanceId) {
        return this.runtimeService.getVariables(processInstanceId);
    }

    public void setVariable(String executionId, String variableName, Object value) {
        this.runtimeService.setVariable(executionId, variableName, value);
    }

    public void setVariables(String executionId, Map<String, ? extends Object> variables) {
        this.runtimeService.setVariables(executionId, variables);
    }

    public void removeVariable(String executionId, String variableName) {
        this.runtimeService.removeVariable(executionId, variableName);
    }

    public void removeVariables(String executionId, Collection<String> variableNames) {
        this.runtimeService.removeVariables(executionId, variableNames);
    }

    public boolean hasVariable(String executionId, String variableName) {
        return this.runtimeService.hasVariable(executionId, variableName);
    }

    public Object getVariable(String executionId, String variableName) {
        return this.runtimeService.getVariable(executionId, variableName);
    }

    public Map<String, Object> getVariables(String executionId, Collection<String> variableNames) {
        return this.runtimeService.getVariables(executionId, variableNames);
    }

    public void endProcessInstance(String bpmnInstanceId) {
        ActivitiUtil.getCommandExecutor().execute(new ProcessInstanceEndCmd(bpmnInstanceId));
    }

    public void deleteProcessInstance(String bpmnInstId, String reason) {
        this.runtimeService.deleteProcessInstance(bpmnInstId, reason);
    }

    public Object getSuperVariable(String bpmnId, String varName) {
        return ActivitiUtil.getCommandExecutor().execute(new GetSuperVariableCmd(bpmnId, varName));
    }

    public ProcessInstance getProcessInstance(String bpmnInstId) {
        List<ProcessInstance> instances = this.runtimeService.createProcessInstanceQuery().processInstanceId(bpmnInstId).list();
        if (!instances.isEmpty()) {
            return (ProcessInstance) instances.get(0);
        }
        return null;
    }
}
