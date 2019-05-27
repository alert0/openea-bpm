package org.openbpm.bpm.service;

import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.core.util.ThreadMapUtil;
import org.openbpm.bpm.act.img.BpmProcessDiagramGenerator;
import org.openbpm.bpm.api.constant.OpinionStatus;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.service.BpmImageService;
import org.openbpm.bpm.core.manager.BpmInstanceManager;
import org.openbpm.bpm.core.manager.BpmTaskStackManager;
import org.openbpm.bpm.core.model.BpmTaskStack;
import java.awt.Color;
import java.awt.Paint;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultInstHistImgService implements BpmImageService {
    @Autowired
    private BpmInstanceManager bpmInstanceManager;
    @Autowired
    private BpmTaskStackManager bpmTaskStackManager;
    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;
    @Autowired
    private RepositoryService repositoryService;


    public InputStream draw(String actDefId, String actInstId) throws Exception {
        if (StringUtil.isEmpty(actDefId)) {
            throw new BusinessException("流程定义actDefId不能缺失", BpmStatusCode.PARAM_ILLEGAL);
        } else if (StringUtil.isEmpty(actInstId)) {
            return drawByDefId(actDefId);
        } else {
            Map<String, Paint> nodeMap = new HashMap<>();
            Map<String, Paint> flowMap = new HashMap<>();
            Map<String, Paint> gateMap = new HashMap<>();
            for (BpmTaskStack stack : this.bpmTaskStackManager.getByInstanceId(this.bpmInstanceManager.getByActInstId(actInstId).getId())) {
                if ("sequenceFlow".equals(stack.getNodeType())) {
                    flowMap.put(stack.getNodeId(), getOpinionColar(stack.getActionName()));
                } else if ("exclusiveGateway".equals(stack.getNodeType())) {
                    gateMap.put(stack.getNodeId(), getOpinionColar(stack.getActionName()));
                } else {
                    nodeMap.put(stack.getNodeId(), getOpinionColar(stack.getActionName()));
                }
            }
            ThreadMapUtil.put("DefaultInstHistImgService_nodeMap", nodeMap);
            ThreadMapUtil.put("DefaultInstHistImgService_flowMap", flowMap);
            ThreadMapUtil.put("DefaultInstHistImgService_gateMap", gateMap);
            InputStream imageStream = null;
            try {
                imageStream = new BpmProcessDiagramGenerator().generateDiagram(this.repositoryService.getBpmnModel(actDefId), "png", nodeMap, flowMap);
                return imageStream;
            } finally {
                IOUtils.closeQuietly(imageStream);
            }
        }
    }

    private Paint getOpinionColar(String action) {
        if (StringUtil.isEmpty(action)) {
            return Color.GREEN;
        }
        OpinionStatus status = OpinionStatus.fromKey(action);
        if (status == null) {
            return Color.GREEN;
        }
        switch (status) {
            case START:
                return Color.YELLOW;
            case CREATE:
                return Color.RED;
            case END:
                return Color.BLACK;
            case AWAITING_CHECK:
                return Color.RED;
            case AGREE:
                return Color.GREEN;
            case OPPOSE:
                return Color.PINK;
            case ABANDON:
                return Color.GRAY;
            case REJECT:
                return Color.PINK;
            case REJECT_TO_START:
                return Color.PINK;
            case REVOKER:
                return Color.PINK;
            case REVOKER_TO_START:
                return Color.PINK;
            case SKIP:
                return Color.GRAY;
            case MANUAL_END:
                return Color.RED;
            default:
                return Color.RED;
        }
    }


    private InputStream drawByDefId(String actDefId) {
        return this.processEngineConfiguration.getProcessDiagramGenerator().generateDiagram(this.repositoryService.getBpmnModel(actDefId), "png", this.processEngineConfiguration.getActivityFontName(), this.processEngineConfiguration.getLabelFontName(), this.processEngineConfiguration.getAnnotationFontName(), this.processEngineConfiguration.getClassLoader());
    }
}
