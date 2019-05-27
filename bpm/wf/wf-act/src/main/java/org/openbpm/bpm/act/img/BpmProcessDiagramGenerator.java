package org.openbpm.bpm.act.img;

import org.openbpm.base.core.util.AppUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.core.util.ThreadMapUtil;
import java.awt.Paint;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.Artifact;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.CallActivity;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowElementsContainer;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Gateway;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.bpmn.model.Lane;
import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;
import org.activiti.bpmn.model.Pool;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
//import org.activiti.image.impl.DefaultProcessDiagramGenerator.ActivityDrawInstruction;

public class BpmProcessDiagramGenerator extends DefaultProcessDiagramGenerator {
    private static ProcessEngineConfiguration processEngineConfiguration;

    public InputStream generateDiagram(BpmnModel bpmnModel, String imageType, Map<String, Paint> nodeMap, Map<String, Paint> flowMap) {
        prepareBpmnModel(bpmnModel);
        BpmProcessDiagramCanvas processDiagramCanvas = initProcessDiagramCanvas(bpmnModel, imageType, processEngineConfiguration().getActivityFontName(), processEngineConfiguration().getActivityFontName(), processEngineConfiguration().getAnnotationFontName(), processEngineConfiguration().getClassLoader());
        for (Pool pool : bpmnModel.getPools()) {
            processDiagramCanvas.drawPoolOrLane(pool.getName(), bpmnModel.getGraphicInfo(pool.getId()));
        }
        for (Process process : bpmnModel.getProcesses()) {
            for (Lane lane : process.getLanes()) {
                processDiagramCanvas.drawPoolOrLane(lane.getName(), bpmnModel.getGraphicInfo(lane.getId()));
            }
        }
        for (Process findFlowElementsOfType : bpmnModel.getProcesses()) {
            for (FlowNode flowNode : findFlowElementsOfType.findFlowElementsOfType(FlowNode.class)) {
                drawActivity(processDiagramCanvas, bpmnModel, flowNode, nodeMap, flowMap, 1.0d);
            }
        }
        for (Process process2 : bpmnModel.getProcesses()) {
            for (Artifact artifact : process2.getArtifacts()) {
                drawArtifact(processDiagramCanvas, bpmnModel, artifact);
            }
            List<SubProcess> subProcesses = process2.findFlowElementsOfType(SubProcess.class, true);
            if (subProcesses != null) {
                for (SubProcess subProcess : subProcesses) {
                    for (Artifact subProcessArtifact : subProcess.getArtifacts()) {
                        drawArtifact(processDiagramCanvas, bpmnModel, subProcessArtifact);
                    }
                }
            }
        }
        return processDiagramCanvas.generateImage(imageType);
    }

    private void drawActivity(BpmProcessDiagramCanvas processDiagramCanvas, BpmnModel bpmnModel, FlowNode flowNode, Map<String, Paint> nodes, Map<String, Paint> flows, double scaleFactor) {
        ThreadMapUtil.put("BpmProcessDiagramGenerator_flowNode", flowNode);
        ActivityDrawInstruction drawInstruction = (ActivityDrawInstruction) this.activityDrawInstructions.get(flowNode.getClass());
        if (drawInstruction != null) {
            drawInstruction.draw(processDiagramCanvas, bpmnModel, flowNode);
            boolean multiInstanceSequential = false;
            boolean multiInstanceParallel = false;
            boolean collapsed = false;
            if (flowNode instanceof Activity) {
                MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = ((Activity) flowNode).getLoopCharacteristics();
                if (multiInstanceLoopCharacteristics != null) {
                    multiInstanceSequential = multiInstanceLoopCharacteristics.isSequential();
                    multiInstanceParallel = !multiInstanceSequential;
                }
            }
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            if (flowNode instanceof SubProcess) {
                collapsed = graphicInfo.getExpanded() != null && !graphicInfo.getExpanded().booleanValue();
            } else if (flowNode instanceof CallActivity) {
                collapsed = true;
            }
            if (scaleFactor == 1.0d) {
                processDiagramCanvas.drawActivityMarkers((int) graphicInfo.getX(), (int) graphicInfo.getY(), (int) graphicInfo.getWidth(), (int) graphicInfo.getHeight(), multiInstanceSequential, multiInstanceParallel, collapsed);
            }
            if (nodes.keySet().contains(flowNode.getId())) {
                drawHighLight(processDiagramCanvas, bpmnModel.getGraphicInfo(flowNode.getId()), (Paint) nodes.get(flowNode.getId()));
            }
        }
        for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
            boolean highLighted = flows.keySet().contains(sequenceFlow.getId());
            String defaultFlow = null;
            if (flowNode instanceof Activity) {
                defaultFlow = ((Activity) flowNode).getDefaultFlow();
            } else if (flowNode instanceof Gateway) {
                defaultFlow = ((Gateway) flowNode).getDefaultFlow();
            }
            boolean isDefault = false;
            if (defaultFlow != null) {
                if (defaultFlow.equalsIgnoreCase(sequenceFlow.getId())) {
                    isDefault = true;
                }
            }
            boolean drawConditionalIndicator = sequenceFlow.getConditionExpression() != null && !(flowNode instanceof Gateway);
            String sourceRef = sequenceFlow.getSourceRef();
            String targetRef = sequenceFlow.getTargetRef();
            FlowElement sourceElement = bpmnModel.getFlowElement(sourceRef);
            FlowElement targetElement = bpmnModel.getFlowElement(targetRef);
            List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
            if (graphicInfoList != null && graphicInfoList.size() > 0) {
                List<GraphicInfo> graphicInfoList2 = connectionPerfectionizer(processDiagramCanvas, bpmnModel, sourceElement, targetElement, graphicInfoList);
                int[] xPoints = new int[graphicInfoList2.size()];
                int[] yPoints = new int[graphicInfoList2.size()];
                for (int i = 1; i < graphicInfoList2.size(); i++) {
                    GraphicInfo graphicInfo2 = (GraphicInfo) graphicInfoList2.get(i);
                    GraphicInfo previousGraphicInfo = (GraphicInfo) graphicInfoList2.get(i - 1);
                    if (i == 1) {
                        xPoints[0] = (int) previousGraphicInfo.getX();
                        yPoints[0] = (int) previousGraphicInfo.getY();
                    }
                    xPoints[i] = (int) graphicInfo2.getX();
                    yPoints[i] = (int) graphicInfo2.getY();
                }
                processDiagramCanvas.drawSequenceflow(xPoints, yPoints, drawConditionalIndicator, isDefault, highLighted, (Paint) flows.get(sequenceFlow.getId()), scaleFactor);
                if (StringUtil.isNotEmpty(sequenceFlow.getName()) && !sequenceFlow.getName().startsWith("连线")) {
                    GraphicInfo info = new GraphicInfo();
                    info.setX((double) ((xPoints[0] + xPoints[1]) / 2));
                    info.setY((double) (((yPoints[0] + yPoints[1]) / 2) - 15));
                    processDiagramCanvas.drawLabel(sequenceFlow.getName(), info, false);
                }
                GraphicInfo labelGraphicInfo = bpmnModel.getLabelGraphicInfo(sequenceFlow.getId());
                if (labelGraphicInfo != null) {
                    processDiagramCanvas.drawLabel(sequenceFlow.getName(), labelGraphicInfo, false);
                }
            }
        }
        if (flowNode instanceof FlowElementsContainer) {
            for (FlowElement nestedFlowElement : ((FlowElementsContainer) flowNode).getFlowElements()) {
                if (nestedFlowElement instanceof FlowNode) {
                    drawActivity(processDiagramCanvas, bpmnModel, (FlowNode) nestedFlowElement, nodes, flows, scaleFactor);
                }
            }
        }
        if (flowNode instanceof ExclusiveGateway) {
            GraphicInfo graphicInfo3 = bpmnModel.getGraphicInfo(flowNode.getId());
            GraphicInfo info2 = new GraphicInfo();
            info2.setX(graphicInfo3.getX() + 40.0d);
            info2.setY(graphicInfo3.getY() + 40.0d);
            processDiagramCanvas.drawLabel(flowNode.getName(), info2, true);
        }
    }

    private void drawHighLight(BpmProcessDiagramCanvas processDiagramCanvas, GraphicInfo graphicInfo, Paint paint) {
        processDiagramCanvas.drawHighLight((int) graphicInfo.getX(), (int) graphicInfo.getY(), (int) graphicInfo.getWidth(), (int) graphicInfo.getHeight(), paint);
    }

    protected static BpmProcessDiagramCanvas initProcessDiagramCanvas(BpmnModel bpmnModel, String imageType, String activityFontName, String labelFontName, String annotationFontName, ClassLoader customClassLoader) {
        double minY = 0;
        double minX = 0;
        double maxY = 0;
        double maxX = 0;
        double minY2 = 0;
        double minX2 = 0;
        double maxY2 = 0;
        double maxX2 = 0;
        double minX3 = Double.MAX_VALUE;
        double maxX3 = 0.0d;
        double minY3 = Double.MAX_VALUE;
        double maxY3 = 0.0d;
        for (Pool pool : bpmnModel.getPools()) {
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(pool.getId());
            minX3 = graphicInfo.getX();
            maxX3 = graphicInfo.getX() + graphicInfo.getWidth();
            minY3 = graphicInfo.getY();
            maxY3 = graphicInfo.getY() + graphicInfo.getHeight();
        }
        List<FlowNode> flowNodes = gatherAllFlowNodes(bpmnModel);
        for (FlowNode flowNode : flowNodes) {
            GraphicInfo flowNodeGraphicInfo = bpmnModel.getGraphicInfo(flowNode.getId());
            if (flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth() > maxX) {
                maxX = flowNodeGraphicInfo.getX() + flowNodeGraphicInfo.getWidth();
            }
            if (flowNodeGraphicInfo.getX() < minX) {
                minX = flowNodeGraphicInfo.getX();
            }
            if (flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight() > maxY) {
                maxY = flowNodeGraphicInfo.getY() + flowNodeGraphicInfo.getHeight();
            }
            if (flowNodeGraphicInfo.getY() < minY) {
                minY = flowNodeGraphicInfo.getY();
            }
            for (SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
                List<GraphicInfo> graphicInfoList = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
                if (graphicInfoList != null) {
                    for (GraphicInfo graphicInfo2 : graphicInfoList) {
                        if (graphicInfo2.getX() > maxX) {
                            maxX = graphicInfo2.getX();
                        }
                        if (graphicInfo2.getX() < minX) {
                            minX = graphicInfo2.getX();
                        }
                        if (graphicInfo2.getY() > maxY) {
                            maxY = graphicInfo2.getY();
                        }
                        if (graphicInfo2.getY() < minY) {
                            minY = graphicInfo2.getY();
                        }
                    }
                }
            }
        }
        for (Artifact artifact : gatherAllArtifacts(bpmnModel)) {
            GraphicInfo artifactGraphicInfo = bpmnModel.getGraphicInfo(artifact.getId());
            if (artifactGraphicInfo != null) {
                if (artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth() > maxX) {
                    maxX = artifactGraphicInfo.getX() + artifactGraphicInfo.getWidth();
                }
                if (artifactGraphicInfo.getX() < minX) {
                    minX = artifactGraphicInfo.getX();
                }
                if (artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight() > maxY) {
                    maxY = artifactGraphicInfo.getY() + artifactGraphicInfo.getHeight();
                }
                if (artifactGraphicInfo.getY() < minY) {
                    minY = artifactGraphicInfo.getY();
                }
            }
            List<GraphicInfo> graphicInfoList2 = bpmnModel.getFlowLocationGraphicInfo(artifact.getId());
            if (graphicInfoList2 != null) {
                for (GraphicInfo graphicInfo3 : graphicInfoList2) {
                    if (graphicInfo3.getX() > maxX) {
                        maxX = graphicInfo3.getX();
                    }
                    if (graphicInfo3.getX() < minX) {
                        minX = graphicInfo3.getX();
                    }
                    if (graphicInfo3.getY() > maxY) {
                        maxY = graphicInfo3.getY();
                    }
                    if (graphicInfo3.getY() < minY) {
                        minY = graphicInfo3.getY();
                    }
                }
            }
        }
        int nrOfLanes = 0;
        for (Process process : bpmnModel.getProcesses()) {
            for (Lane l : process.getLanes()) {
                nrOfLanes++;
                GraphicInfo graphicInfo4 = bpmnModel.getGraphicInfo(l.getId());
                if (graphicInfo4.getX() + graphicInfo4.getWidth() > maxX2) {
                    maxX2 = graphicInfo4.getX() + graphicInfo4.getWidth();
                }
                if (graphicInfo4.getX() < minX2) {
                    minX2 = graphicInfo4.getX();
                }
                if (graphicInfo4.getY() + graphicInfo4.getHeight() > maxY2) {
                    maxY2 = graphicInfo4.getY() + graphicInfo4.getHeight();
                }
                if (graphicInfo4.getY() < minY2) {
                    minY2 = graphicInfo4.getY();
                }
            }
        }
        if (flowNodes.isEmpty() && bpmnModel.getPools().isEmpty() && nrOfLanes == 0) {
            minX2 = 0.0d;
            minY2 = 0.0d;
        }
        return new BpmProcessDiagramCanvas(((int) maxX2) + 10, ((int) maxY2) + 10, (int) minX2, (int) minY2, imageType, activityFontName, labelFontName, annotationFontName, customClassLoader);
    }

    private static ProcessEngineConfiguration processEngineConfiguration() {
        if (processEngineConfiguration == null) {
            processEngineConfiguration = (ProcessEngineConfiguration) AppUtil.getBean(ProcessEngineConfiguration.class);
        }
        return processEngineConfiguration;
    }
}
