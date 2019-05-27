package org.openbpm.activiti.rest.diagram.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.activiti.bpmn.model.ErrorEventDefinition;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.bpmn.behavior.BoundaryEventActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.CallActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.EventSubscriptionDeclaration;
import org.activiti.engine.impl.jobexecutor.TimerDeclarationImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.Lane;
import org.activiti.engine.impl.pvm.process.LaneSet;
import org.activiti.engine.impl.pvm.process.ParticipantProcess;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseProcessDefinitionDiagramLayoutResource {
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;

    public ObjectNode getDiagramNode(String processInstanceId, String processDefinitionId) {
        List<String> highLightedFlows = Collections.emptyList();
        List emptyList = Collections.emptyList();
        Map<String, ObjectNode> subProcessInstanceMap = new HashMap<>();
        ProcessInstance processInstance = null;
        if (processInstanceId != null) {
            processInstance = (ProcessInstance) this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            if (processInstance == null) {
                throw new RuntimeException("Process instance could not be found");
            }
            processDefinitionId = processInstance.getProcessDefinitionId();
            for (ProcessInstance subProcessInstance : this.runtimeService.createProcessInstanceQuery().superProcessInstanceId(processInstanceId).list()) {
                String subDefId = subProcessInstance.getProcessDefinitionId();
                String superExecutionId = ((ExecutionEntity) subProcessInstance).getSuperExecutionId();
                ProcessDefinitionEntity subDef = (ProcessDefinitionEntity) this.repositoryService.getProcessDefinition(subDefId);
                ObjectNode processInstanceJSON = new ObjectMapper().createObjectNode();
                processInstanceJSON.put("processInstanceId", subProcessInstance.getId());
                processInstanceJSON.put("superExecutionId", superExecutionId);
                processInstanceJSON.put("processDefinitionId", subDef.getId());
                processInstanceJSON.put("processDefinitionKey", subDef.getKey());
                processInstanceJSON.put("processDefinitionName", subDef.getName());
                subProcessInstanceMap.put(superExecutionId, processInstanceJSON);
            }
        }
        if (processDefinitionId == null) {
            throw new RuntimeException("No process definition id provided");
        }
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) this.repositoryService.getProcessDefinition(processDefinitionId);
        if (processDefinition == null) {
            throw new ActivitiException("Process definition " + processDefinitionId + " could not be found");
        }
        ObjectNode responseJSON = new ObjectMapper().createObjectNode();
        JsonNode pdrJSON = getProcessDefinitionResponse(processDefinition);
        if (pdrJSON != null) {
            responseJSON.put("processDefinition", pdrJSON);
        }
        if (processInstance != null) {
            ArrayNode activityArray = new ObjectMapper().createArrayNode();
            ArrayNode flowsArray = new ObjectMapper().createArrayNode();
            List<String> highLightedActivities = this.runtimeService.getActiveActivityIds(processInstanceId);
            highLightedFlows = getHighLightedFlows(processInstanceId, processDefinition);
            for (String activityName : highLightedActivities) {
                activityArray.add(activityName);
            }
            for (String flow : highLightedFlows) {
                flowsArray.add(flow);
            }
            responseJSON.put("highLightedActivities", activityArray);
            responseJSON.put("highLightedFlows", flowsArray);
        }
        if (processDefinition.getParticipantProcess() != null) {
            ParticipantProcess pProc = processDefinition.getParticipantProcess();
            ObjectNode participantProcessJSON = new ObjectMapper().createObjectNode();
            participantProcessJSON.put("id", pProc.getId());
            if (StringUtils.isNotEmpty(pProc.getName())) {
                participantProcessJSON.put("name", pProc.getName());
            } else {
                participantProcessJSON.put("name", "");
            }
            participantProcessJSON.put("x", pProc.getX());
            participantProcessJSON.put("y", pProc.getY());
            participantProcessJSON.put("width", pProc.getWidth());
            participantProcessJSON.put("height", pProc.getHeight());
            responseJSON.put("participantProcess", participantProcessJSON);
        }
        if (processDefinition.getLaneSets() != null && !processDefinition.getLaneSets().isEmpty()) {
            ArrayNode laneSetArray = new ObjectMapper().createArrayNode();
            for (LaneSet laneSet : processDefinition.getLaneSets()) {
                ArrayNode laneArray = new ObjectMapper().createArrayNode();
                if (laneSet.getLanes() != null && !laneSet.getLanes().isEmpty()) {
                    for (Lane lane : laneSet.getLanes()) {
                        ObjectNode laneJSON = new ObjectMapper().createObjectNode();
                        laneJSON.put("id", lane.getId());
                        if (StringUtils.isNotEmpty(lane.getName())) {
                            laneJSON.put("name", lane.getName());
                        } else {
                            laneJSON.put("name", "");
                        }
                        laneJSON.put("x", lane.getX());
                        laneJSON.put("y", lane.getY());
                        laneJSON.put("width", lane.getWidth());
                        laneJSON.put("height", lane.getHeight());
                        List<String> flowNodeIds = lane.getFlowNodeIds();
                        ArrayNode flowNodeIdsArray = new ObjectMapper().createArrayNode();
                        for (String flowNodeId : flowNodeIds) {
                            flowNodeIdsArray.add(flowNodeId);
                        }
                        laneJSON.put("flowNodeIds", flowNodeIdsArray);
                        laneArray.add(laneJSON);
                    }
                }
                ObjectNode laneSetJSON = new ObjectMapper().createObjectNode();
                laneSetJSON.put("id", laneSet.getId());
                if (StringUtils.isNotEmpty(laneSet.getName())) {
                    laneSetJSON.put("name", laneSet.getName());
                } else {
                    laneSetJSON.put("name", "");
                }
                laneSetJSON.put("lanes", laneArray);
                laneSetArray.add(laneSetJSON);
            }
            if (laneSetArray.size() > 0) {
                responseJSON.put("laneSets", laneSetArray);
            }
        }
        ArrayNode sequenceFlowArray = new ObjectMapper().createArrayNode();
        ArrayNode activityArray2 = new ObjectMapper().createArrayNode();
        for (ActivityImpl activity : processDefinition.getActivities()) {
            getActivity(processInstanceId, activity, activityArray2, sequenceFlowArray, processInstance, highLightedFlows, subProcessInstanceMap);
        }
        responseJSON.put("activities", activityArray2);
        responseJSON.put("sequenceFlows", sequenceFlowArray);
        return responseJSON;
    }

    private List<String> getHighLightedFlows(String processInstanceId, ProcessDefinitionEntity processDefinition) {
        List<String> highLightedFlows = new ArrayList<>();
        List<HistoricActivityInstance> historicActivityInstances = this.historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
        List<String> historicActivityInstanceList = new ArrayList<>();
        for (HistoricActivityInstance hai : historicActivityInstances) {
            historicActivityInstanceList.add(hai.getActivityId());
        }
        historicActivityInstanceList.addAll(this.runtimeService.getActiveActivityIds(processInstanceId));
        for (ActivityImpl activity : processDefinition.getActivities()) {
            int index = historicActivityInstanceList.indexOf(activity.getId());
            if (index >= 0 && index + 1 < historicActivityInstanceList.size()) {
                for (PvmTransition pvmTransition : activity.getOutgoingTransitions()) {
                    if (pvmTransition.getDestination().getId().equals(historicActivityInstanceList.get(index + 1))) {
                        highLightedFlows.add(pvmTransition.getId());
                    }
                }
            }
        }
        return highLightedFlows;
    }

    private void getActivity(String processInstanceId, ActivityImpl activity, ArrayNode activityArray, ArrayNode sequenceFlowArray, ProcessInstance processInstance, List<String> highLightedFlows, Map<String, ObjectNode> subProcessInstanceMap) {
        boolean isConditional = false;
        boolean isDefault = false;
        List<Integer> waypoints;
        int i = 0;
        ObjectNode activityJSON = new ObjectMapper().createObjectNode();
        String multiInstance = (String) activity.getProperty("multiInstance");
        if (multiInstance != null && !"sequential".equals(multiInstance)) {
            multiInstance = "parallel";
        }
        ActivityBehavior activityBehavior = activity.getActivityBehavior();
        Boolean collapsed = Boolean.valueOf(activityBehavior instanceof CallActivityBehavior);
        Boolean expanded = (Boolean) activity.getProperty("isExpanded");
        if (expanded != null) {
            collapsed = Boolean.valueOf(!expanded.booleanValue());
        }
        Boolean isInterrupting = null;
        if (activityBehavior instanceof BoundaryEventActivityBehavior) {
            isInterrupting = Boolean.valueOf(((BoundaryEventActivityBehavior) activityBehavior).isInterrupting());
        }
        for (PvmTransition sequenceFlow : activity.getOutgoingTransitions()) {
            String flowName = (String) sequenceFlow.getProperty("name");
            boolean isHighLighted = highLightedFlows.contains(sequenceFlow.getId());
            if (sequenceFlow.getProperty("condition") != null) {
                if (!((String) activity.getProperty("type")).toLowerCase().contains("gateway")) {
                    isConditional = true;
                    if (sequenceFlow.getId().equals(activity.getProperty("default"))) {
                        if (((String) activity.getProperty("type")).toLowerCase().contains("gateway")) {
                            isDefault = true;
                            waypoints = ((TransitionImpl) sequenceFlow).getWaypoints();
                            ArrayNode xPointArray = new ObjectMapper().createArrayNode();
                            ArrayNode yPointArray = new ObjectMapper().createArrayNode();
                            for (i = 0; i < waypoints.size(); i += 2) {
                                xPointArray.add((Integer) waypoints.get(i));
                                yPointArray.add((Integer) waypoints.get(i + 1));
                            }
                            ObjectNode flowJSON = new ObjectMapper().createObjectNode();
                            flowJSON.put("id", sequenceFlow.getId());
                            flowJSON.put("name", flowName);
                            flowJSON.put("flow", "(" + sequenceFlow.getSource().getId() + ")--" + sequenceFlow.getId() + "-->(" + sequenceFlow.getDestination().getId() + ")");
                            if (isConditional) {
                                flowJSON.put("isConditional", isConditional);
                            }
                            if (isDefault) {
                                flowJSON.put("isDefault", isDefault);
                            }
                            if (isHighLighted) {
                                flowJSON.put("isHighLighted", isHighLighted);
                            }
                            flowJSON.put("xPointArray", xPointArray);
                            flowJSON.put("yPointArray", yPointArray);
                            sequenceFlowArray.add(flowJSON);
                        }
                    }
                    isDefault = false;
                    waypoints = ((TransitionImpl) sequenceFlow).getWaypoints();
                    ArrayNode xPointArray2 = new ObjectMapper().createArrayNode();
                    ArrayNode yPointArray2 = new ObjectMapper().createArrayNode();
                    while (i < waypoints.size()) {
                    }
                    ObjectNode flowJSON2 = new ObjectMapper().createObjectNode();
                    flowJSON2.put("id", sequenceFlow.getId());
                    flowJSON2.put("name", flowName);
                    flowJSON2.put("flow", "(" + sequenceFlow.getSource().getId() + ")--" + sequenceFlow.getId() + "-->(" + sequenceFlow.getDestination().getId() + ")");
                    if (isConditional) {
                    }
                    if (isDefault) {
                    }
                    if (isHighLighted) {
                    }
                    flowJSON2.put("xPointArray", xPointArray2);
                    flowJSON2.put("yPointArray", yPointArray2);
                    sequenceFlowArray.add(flowJSON2);
                }
            }
            isConditional = false;
            if (sequenceFlow.getId().equals(activity.getProperty("default"))) {
            }
            isDefault = false;
            waypoints = ((TransitionImpl) sequenceFlow).getWaypoints();
            ArrayNode xPointArray22 = new ObjectMapper().createArrayNode();
            ArrayNode yPointArray22 = new ObjectMapper().createArrayNode();
            while (i < waypoints.size()) {
            }
            ObjectNode flowJSON22 = new ObjectMapper().createObjectNode();
            flowJSON22.put("id", sequenceFlow.getId());
            flowJSON22.put("name", flowName);
            flowJSON22.put("flow", "(" + sequenceFlow.getSource().getId() + ")--" + sequenceFlow.getId() + "-->(" + sequenceFlow.getDestination().getId() + ")");
            // TODO will check
            if (isConditional) {
            }
            if (isDefault) {
            }
            if (isHighLighted) {
            }
            flowJSON22.put("xPointArray", xPointArray22);
            flowJSON22.put("yPointArray", yPointArray22);
            sequenceFlowArray.add(flowJSON22);
        }
        ArrayNode nestedActivityArray = new ObjectMapper().createArrayNode();
        for (ActivityImpl nestedActivity : activity.getActivities()) {
            nestedActivityArray.add(nestedActivity.getId());
        }
        Map<String, Object> properties = activity.getProperties();
        ObjectNode propertiesJSON = new ObjectMapper().createObjectNode();
        for (String key : properties.keySet()) {
            Object prop = properties.get(key);
            if (prop instanceof String) {
                propertiesJSON.put(key, (String) properties.get(key));
            } else if (prop instanceof Integer) {
                propertiesJSON.put(key, (Integer) properties.get(key));
            } else if (prop instanceof Boolean) {
                propertiesJSON.put(key, (Boolean) properties.get(key));
            } else if ("initial".equals(key)) {
                propertiesJSON.put(key, ((ActivityImpl) properties.get(key)).getId());
            } else if ("timerDeclarations".equals(key)) {
                ArrayList<TimerDeclarationImpl> timerDeclarations = (ArrayList) properties.get(key);
                ArrayNode timerDeclarationArray = new ObjectMapper().createArrayNode();
                if (timerDeclarations != null) {
                    Iterator it = timerDeclarations.iterator();
                    while (it.hasNext()) {
                        TimerDeclarationImpl timerDeclaration = (TimerDeclarationImpl) it.next();
                        ObjectNode timerDeclarationJSON = new ObjectMapper().createObjectNode();
                        timerDeclarationJSON.put("isExclusive", timerDeclaration.isExclusive());
                        if (timerDeclaration.getRepeat() != null) {
                            timerDeclarationJSON.put("repeat", timerDeclaration.getRepeat());
                        }
                        timerDeclarationJSON.put("retries", String.valueOf(timerDeclaration.getRetries()));
                        timerDeclarationJSON.put("type", timerDeclaration.getJobHandlerType());
                        timerDeclarationJSON.put("configuration", timerDeclaration.getJobHandlerConfiguration());
                        timerDeclarationArray.add(timerDeclarationJSON);
                    }
                }
                if (timerDeclarationArray.size() > 0) {
                    propertiesJSON.put(key, timerDeclarationArray);
                }
            } else if ("eventDefinitions".equals(key)) {
                ArrayList<EventSubscriptionDeclaration> eventDefinitions = (ArrayList) properties.get(key);
                ArrayNode eventDefinitionsArray = new ObjectMapper().createArrayNode();
                if (eventDefinitions != null) {
                    Iterator it2 = eventDefinitions.iterator();
                    while (it2.hasNext()) {
                        EventSubscriptionDeclaration eventDefinition = (EventSubscriptionDeclaration) it2.next();
                        ObjectNode eventDefinitionJSON = new ObjectMapper().createObjectNode();
                        if (eventDefinition.getActivityId() != null) {
                            eventDefinitionJSON.put("activityId", eventDefinition.getActivityId());
                        }
                        eventDefinitionJSON.put("eventName", eventDefinition.getEventName());
                        eventDefinitionJSON.put("eventType", eventDefinition.getEventType());
                        eventDefinitionJSON.put("isAsync", eventDefinition.isAsync());
                        eventDefinitionJSON.put("isStartEvent", eventDefinition.isStartEvent());
                        eventDefinitionsArray.add(eventDefinitionJSON);
                    }
                }
                if (eventDefinitionsArray.size() > 0) {
                    propertiesJSON.put(key, eventDefinitionsArray);
                }
            } else if ("errorEventDefinitions".equals(key)) {
                ArrayList<ErrorEventDefinition> errorEventDefinitions = (ArrayList) properties.get(key);
                ArrayNode errorEventDefinitionsArray = new ObjectMapper().createArrayNode();
                if (errorEventDefinitions != null) {
                    Iterator it3 = errorEventDefinitions.iterator();
                    while (it3.hasNext()) {
                        ErrorEventDefinition errorEventDefinition = (ErrorEventDefinition) it3.next();
                        ObjectNode errorEventDefinitionJSON = new ObjectMapper().createObjectNode();
                        if (errorEventDefinition.getErrorCode() != null) {
                            errorEventDefinitionJSON.put("errorCode", errorEventDefinition.getErrorCode());
                        } else {
                            errorEventDefinitionJSON.putNull("errorCode");
                        }
                        errorEventDefinitionJSON.put("handlerActivityId", errorEventDefinition.getId());
                        errorEventDefinitionsArray.add(errorEventDefinitionJSON);
                    }
                }
                if (errorEventDefinitionsArray.size() > 0) {
                    propertiesJSON.put(key, errorEventDefinitionsArray);
                }
            }
        }
        if ("callActivity".equals(properties.get("type"))) {
            CallActivityBehavior callActivityBehavior = null;
            if (activityBehavior instanceof CallActivityBehavior) {
                callActivityBehavior = (CallActivityBehavior) activityBehavior;
            }
            if (callActivityBehavior != null) {
                propertiesJSON.put("processDefinitonKey", callActivityBehavior.getProcessDefinitonKey());
                ArrayNode processInstanceArray = new ObjectMapper().createArrayNode();
                if (processInstance != null) {
                    List<Execution> executionList = this.runtimeService.createExecutionQuery().processInstanceId(processInstanceId).activityId(activity.getId()).list();
                    if (!executionList.isEmpty()) {
                        for (Execution execution : executionList) {
                            processInstanceArray.add((ObjectNode) subProcessInstanceMap.get(execution.getId()));
                        }
                    }
                }
                if (processInstanceArray.size() == 0) {
                    ProcessDefinition lastProcessDefinition = (ProcessDefinition) this.repositoryService.createProcessDefinitionQuery().processDefinitionKey(callActivityBehavior.getProcessDefinitonKey()).latestVersion().singleResult();
                    ObjectNode processInstanceJSON = new ObjectMapper().createObjectNode();
                    processInstanceJSON.put("processDefinitionId", lastProcessDefinition.getId());
                    processInstanceJSON.put("processDefinitionKey", lastProcessDefinition.getKey());
                    processInstanceJSON.put("processDefinitionName", lastProcessDefinition.getName());
                    processInstanceArray.add(processInstanceJSON);
                }
                if (processInstanceArray.size() > 0) {
                    propertiesJSON.put("processDefinitons", processInstanceArray);
                }
            }
        }
        activityJSON.put("activityId", activity.getId());
        activityJSON.put("properties", propertiesJSON);
        if (multiInstance != null) {
            activityJSON.put("multiInstance", multiInstance);
        }
        if (collapsed.booleanValue()) {
            activityJSON.put("collapsed", collapsed);
        }
        if (nestedActivityArray.size() > 0) {
            activityJSON.put("nestedActivities", nestedActivityArray);
        }
        if (isInterrupting != null) {
            activityJSON.put("isInterrupting", isInterrupting);
        }
        activityJSON.put("x", activity.getX());
        activityJSON.put("y", activity.getY());
        activityJSON.put("width", activity.getWidth());
        activityJSON.put("height", activity.getHeight());
        activityArray.add(activityJSON);
        for (ActivityImpl nestedActivity2 : activity.getActivities()) {
            getActivity(processInstanceId, nestedActivity2, activityArray, sequenceFlowArray, processInstance, highLightedFlows, subProcessInstanceMap);
        }
    }

    private JsonNode getProcessDefinitionResponse(ProcessDefinitionEntity processDefinition) {
        ObjectNode pdrJSON = new ObjectMapper().createObjectNode();
        pdrJSON.put("id", processDefinition.getId());
        pdrJSON.put("name", processDefinition.getName());
        pdrJSON.put("key", processDefinition.getKey());
        pdrJSON.put("version", processDefinition.getVersion());
        pdrJSON.put("deploymentId", processDefinition.getDeploymentId());
        pdrJSON.put("isGraphicNotationDefined", isGraphicNotationDefined(processDefinition));
        return pdrJSON;
    }

    private boolean isGraphicNotationDefined(ProcessDefinitionEntity processDefinition) {
        return ((org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity)this.repositoryService.getProcessDefinition(processDefinition.getId())).isGraphicalNotationDefined();
    }
}
