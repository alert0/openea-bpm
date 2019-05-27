package org.openbpm.bpm.engine.parser;

import org.openbpm.bpm.api.constant.NodeType;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.api.model.nodedef.impl.BaseBpmNodeDef;
import org.openbpm.bpm.api.model.nodedef.impl.CallActivityNodeDef;
import org.openbpm.bpm.api.model.nodedef.impl.GateWayBpmNodeDef;
import org.openbpm.bpm.api.model.nodedef.impl.SubProcessNodeDef;
import org.openbpm.bpm.api.model.nodedef.impl.UserTaskNodeDef;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.CallActivity;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Gateway;
import org.activiti.bpmn.model.InclusiveGateway;
import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;
import org.activiti.bpmn.model.ParallelGateway;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.bpmn.model.UserTask;
import org.springframework.stereotype.Component;

@Component
public class BpmDefNodeHandler {

    //Class<FlowElement>[]
    private Class[] aryNodeElement = {
            StartEvent.class,
            EndEvent.class,
            ParallelGateway.class,
            InclusiveGateway.class,
            ExclusiveGateway.class,
            UserTask.class,
            ServiceTask.class,
            CallActivity.class,
            SubProcess.class};

    public void setProcessDefNodes(DefaultBpmProcessDef bpmProcessDef, Collection<FlowElement> collection) {
        setProcessDefNodes(null, collection, bpmProcessDef);
    }

    private void setProcessDefNodes(BpmNodeDef parentNodeDef, Collection<FlowElement> flowElementList, DefaultBpmProcessDef bpmProcessDef) {
        Map<String, FlowElement> nodeMap = getNodeList(flowElementList);
        List<SequenceFlow> seqList = getSequenceFlowList(flowElementList);
        Map<String, BpmNodeDef> nodeDefMap = getBpmNodeDef(nodeMap, parentNodeDef, bpmProcessDef);
        setRelateNodeDef(nodeDefMap, seqList);
        List<BpmNodeDef> nodeDefList = new ArrayList<>(nodeDefMap.values());
        bpmProcessDef.setBpmnNodeDefs(nodeDefList);
        for (BpmNodeDef nodeDef : nodeDefList) {
            ((BaseBpmNodeDef) nodeDef).setBpmProcessDef(bpmProcessDef);
        }
    }

    private Map<String, BpmNodeDef> getBpmNodeDef(Map<String, FlowElement> nodeMap, BpmNodeDef parentNodeDef, DefaultBpmProcessDef bpmProcessDef) {
        Map<String, BpmNodeDef> map = new HashMap<>();
        for (Entry<String, FlowElement> ent : nodeMap.entrySet()) {
            map.put(ent.getKey(), getNodeDef(parentNodeDef, (FlowElement) ent.getValue(), bpmProcessDef));
        }
        return map;
    }

    private void setRelateNodeDef(Map<String, BpmNodeDef> nodeDefMap, List<SequenceFlow> seqList) {
        for (SequenceFlow seq : seqList) {
            BpmNodeDef sourceDef = (BpmNodeDef) nodeDefMap.get(seq.getSourceRef());
            BpmNodeDef targetDef = (BpmNodeDef) nodeDefMap.get(seq.getTargetRef());
            sourceDef.addOutcomeNode(targetDef);
            targetDef.addIncomeNode(sourceDef);
        }
    }

    private Map<String, FlowElement> getNodeList(Collection<FlowElement> flowElementList) {
        Map<String, FlowElement> map = new HashMap<>();
        for (FlowElement flowElement : flowElementList) {
            addNode(flowElement, map, this.aryNodeElement);
        }
        return map;
    }

    private BaseBpmNodeDef getNodeDef(BpmNodeDef parentNodeDef, FlowElement flowElement, DefaultBpmProcessDef bpmProcessDef) {
        BaseBpmNodeDef nodeDef = null;
        if (flowElement instanceof Activity) {
            String multi = getNodeDefLoop((Activity) flowElement);
            if (flowElement instanceof UserTask) {
                if (multi == null) {
                    nodeDef = new UserTaskNodeDef();
                    nodeDef.setType(NodeType.USERTASK);
                }
            } else if (!(flowElement instanceof ServiceTask)) {
                if (flowElement instanceof CallActivity) {
                    BaseBpmNodeDef callActivityNodeDef = new CallActivityNodeDef();
                    String flowKey = ((CallActivity) flowElement).getCalledElement();
                    callActivityNodeDef.setType(NodeType.CALLACTIVITY);
                    //TODO add method setFlowKey
                    //callActivityNodeDef.setFlowKey(flowKey);
                    nodeDef = callActivityNodeDef;
                } else if (flowElement instanceof SubProcess) {
                    BaseBpmNodeDef subProcessNodeDef = new SubProcessNodeDef();
                    nodeDef = subProcessNodeDef;
                    nodeDef.setNodeId(flowElement.getId());
                    nodeDef.setName(flowElement.getName());
                    nodeDef.setParentBpmNodeDef(parentNodeDef);
                    subProcessNodeDef.setBpmProcessDef(bpmProcessDef);
                    handSubProcess(nodeDef, (SubProcess) flowElement, bpmProcessDef);
                }
            }
        } else if (flowElement instanceof StartEvent) {
            nodeDef = new BaseBpmNodeDef();
            nodeDef.setType(NodeType.START);
        } else if (flowElement instanceof EndEvent) {
            nodeDef = new BaseBpmNodeDef();
            nodeDef.setType(NodeType.END);
        } else if (flowElement instanceof Gateway) {
            nodeDef = new GateWayBpmNodeDef();
            if (flowElement instanceof ParallelGateway) {
                nodeDef.setType(NodeType.PARALLELGATEWAY);
            } else if (flowElement instanceof InclusiveGateway) {
                nodeDef.setType(NodeType.INCLUSIVEGATEWAY);
            } else if (flowElement instanceof ExclusiveGateway) {
                nodeDef.setType(NodeType.EXCLUSIVEGATEWAY);
            }
        }
        nodeDef.setParentBpmNodeDef(parentNodeDef);
        nodeDef.setNodeId(flowElement.getId());
        nodeDef.setName(flowElement.getName());
        return nodeDef;
    }

    private void handSubProcess(BaseBpmNodeDef nodeDef, SubProcess subProcess, DefaultBpmProcessDef parentProcessDef) {
        DefaultBpmProcessDef bpmProcessDef = new DefaultBpmProcessDef();
        bpmProcessDef.setProcessDefinitionId(subProcess.getId());
        bpmProcessDef.setName(subProcess.getName());
        bpmProcessDef.setDefKey(subProcess.getId());
        bpmProcessDef.setParentProcessDef(parentProcessDef);
        SubProcessNodeDef subNodeDef = (SubProcessNodeDef) nodeDef;
        subNodeDef.setBpmProcessDef(parentProcessDef);
        subNodeDef.setChildBpmProcessDef(bpmProcessDef);
        setProcessDefNodes(nodeDef, subProcess.getFlowElements(), bpmProcessDef);
    }

    private void addNode(FlowElement flowElement, Map<String, FlowElement> map, Class<? extends FlowElement>... flowTypes) {
        for (Class<? extends FlowElement> flowType : flowTypes) {
            if (flowType.isInstance(flowElement)) {
                map.put(flowElement.getId(), flowElement);
                return;
            }
        }
    }

    private String getNodeDefLoop(Activity flowElement) {
        MultiInstanceLoopCharacteristics jaxbloop = flowElement.getLoopCharacteristics();
        if (jaxbloop == null) {
            return null;
        }
        return jaxbloop.isSequential() ? "sequence" : "parallel";
    }

    private List<SequenceFlow> getSequenceFlowList(Collection<FlowElement> flowElementList) {
        List<SequenceFlow> nodeList = new ArrayList<>();
        for (FlowElement flowElement : flowElementList) {
            if (flowElement instanceof SequenceFlow) {
                nodeList.add((SequenceFlow) flowElement);
            }
        }
        return nodeList;
    }
}
