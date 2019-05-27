package org.openbpm.bpm.service;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.api.exception.SerializeException;
import org.openbpm.base.core.cache.ICache;
import org.openbpm.bpm.api.constant.NodeType;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.model.def.BpmProcessDef;
import org.openbpm.bpm.api.model.def.IBpmDefinition;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.api.model.nodedef.impl.SubProcessNodeDef;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.bpm.core.manager.BpmDefinitionManager;
import org.openbpm.bpm.core.model.BpmDefinition;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import org.openbpm.bpm.engine.parser.BpmDefNodeHandler;
import org.openbpm.bpm.engine.parser.BpmProcessDefParser;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultBpmProcessDefService implements BpmProcessDefService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultBpmProcessDefService.class);
    @Resource
    private BpmDefNodeHandler bpmDefNodeHandler;
    @Resource
    private BpmDefinitionManager bpmDefinitionManager;
    @Resource
    ICache<DefaultBpmProcessDef> iCache;
    @Resource
    RepositoryService repositoryService;

    public BpmProcessDef getBpmProcessDef(String processDefId) {
        return getProcessDefByDefId(processDefId);
    }

    public List<BpmNodeDef> getNodeDefs(String processDefinitionId) {
        return getProcessDefByDefId(processDefinitionId).getBpmnNodeDefs();
    }

    public BpmNodeDef getBpmNodeDef(String processDefinitionId, String nodeId) {
        List<BpmNodeDef> list = getNodeDefs(processDefinitionId);
        List<SubProcessNodeDef> listSub = new ArrayList<>();
        for (BpmNodeDef nodeDef : list) {
            if (nodeDef.getNodeId().equals(nodeId)) {
                return nodeDef;
            }
            if (nodeDef instanceof SubProcessNodeDef) {
                listSub.add((SubProcessNodeDef) nodeDef);
            }
        }
        if (listSub.size() > 0) {
            return findSubProcessNodeDefByNodeId(listSub, nodeId);
        }
        return null;
    }

    private BpmNodeDef findSubProcessNodeDefByNodeId(List<SubProcessNodeDef> subList, String nodeId) {
        for (SubProcessNodeDef nodeDef : subList) {
            List<BpmNodeDef> nodeList = nodeDef.getChildBpmProcessDef().getBpmnNodeDefs();
            List<SubProcessNodeDef> nestSub = new ArrayList<>();
            for (BpmNodeDef tmpDef : nodeList) {
                if (tmpDef.getNodeId().equals(nodeId)) {
                    return tmpDef;
                }
                if (tmpDef instanceof SubProcessNodeDef) {
                    nestSub.add((SubProcessNodeDef) tmpDef);
                }
            }
            if (nestSub.size() > 0) {
                return findSubProcessNodeDefByNodeId(nestSub, nodeId);
            }
        }
        return null;
    }

    public BpmNodeDef getStartEvent(String processDefId) {
        for (BpmNodeDef nodeDef : getProcessDefByDefId(processDefId).getBpmnNodeDefs()) {
            if (nodeDef.getType().equals(NodeType.START)) {
                return nodeDef;
            }
        }
        return null;
    }

    public List<BpmNodeDef> getEndEvents(String processDefId) {
        List<BpmNodeDef> nodeList = new ArrayList<>();
        for (BpmNodeDef nodeDef : getProcessDefByDefId(processDefId).getBpmnNodeDefs()) {
            if (nodeDef.getType().equals(NodeType.END)) {
                nodeList.add(nodeDef);
            }
        }
        return nodeList;
    }

    public void clean(String processDefId) {
        this.iCache.delByKey("procdef_" + processDefId);
        BpmContext.cleanTread();
    }

    public List<BpmNodeDef> getStartNodes(String processDefId) {
        return getStartEvent(processDefId).getOutcomeNodes();
    }

    public boolean isStartNode(String defId, String nodeId) {
        for (BpmNodeDef node : getStartNodes(defId)) {
            if (node.getNodeId().equals(nodeId)) {
                return true;
            }
        }
        return false;
    }

    public boolean validNodeDefType(String defId, String nodeId, NodeType nodeDefType) {
        return getBpmNodeDef(defId, nodeId).getType().equals(nodeDefType);
    }

    public boolean isContainCallActivity(String defId) {
        for (BpmNodeDef nodeDef : getProcessDefByDefId(defId).getBpmnNodeDefs()) {
            if (nodeDef.getType().equals(NodeType.CALLACTIVITY)) {
                return true;
            }
        }
        return false;
    }

    private DefaultBpmProcessDef getProcessDefByDefId(String processDefinitionId) {
        DefaultBpmProcessDef processDef = (DefaultBpmProcessDef) BpmContext.getProcessDef(processDefinitionId);
        if (processDef != null) {
            DefaultBpmProcessDef defaultBpmProcessDef = processDef;
            return processDef;
        }
        DefaultBpmProcessDef processDef2 = getProcessDefByDefId(processDefinitionId, Boolean.valueOf(true));
        if (processDef2 == null) {
            DefaultBpmProcessDef defaultBpmProcessDef2 = processDef2;
            return null;
        }
        BpmContext.addProcessDef(processDefinitionId, processDef2);
        DefaultBpmProcessDef defaultBpmProcessDef3 = processDef2;
        return processDef2;
    }

    private synchronized DefaultBpmProcessDef getProcessDefByDefId(String processDefinitionId, Boolean isCache) {
        DefaultBpmProcessDef bpmProcessDef;
        DefaultBpmProcessDef bpmProcessDef2 = null;
        if (isCache.booleanValue()) {
            try {
                bpmProcessDef2 = (DefaultBpmProcessDef) this.iCache.getByKey("procdef_" + processDefinitionId);
            } catch (SerializeException e) {
                this.iCache.delByKey("procdef_" + processDefinitionId);
                bpmProcessDef2 = null;
            }
        }
        if (bpmProcessDef2 != null) {
            DefaultBpmProcessDef defaultBpmProcessDef = bpmProcessDef2;
            bpmProcessDef = bpmProcessDef2;
        } else {
            BpmDefinition bpmDef = (BpmDefinition) this.bpmDefinitionManager.get(processDefinitionId);
            if (bpmDef == null) {
                throw new BusinessException(BpmStatusCode.DEF_LOST);
            }
            Assert.notEmpty(bpmDef.getActDefId(), "BpmDefinition actDefId cannot empty ！ 可能原因为：尚未完成流程设计，请检查流程定义 ", new Object[0]);
            DefaultBpmProcessDef bpmProcessDef3 = initBpmProcessDef(bpmDef);
            if (isCache.booleanValue()) {
                this.iCache.add("procdef_" + processDefinitionId, bpmProcessDef3);
            }
            DefaultBpmProcessDef defaultBpmProcessDef2 = bpmProcessDef3;
            bpmProcessDef = bpmProcessDef3;
        }
        return bpmProcessDef;
    }

    public DefaultBpmProcessDef initBpmProcessDef(BpmDefinition bpmDef) {
        if (bpmDef == null) {
            return null;
        }
        JSONObject bpmDefSetting = JSONObject.parseObject(bpmDef.getDefSetting());
        if (bpmDefSetting == null) {
            throw new BusinessException("流程配置 bpmDefSetting 丢失！请检查流程定义 " + bpmDef.getKey());
        }
        DefaultBpmProcessDef bpmProcessDef = new DefaultBpmProcessDef();
        bpmProcessDef.setProcessDefinitionId(bpmDef.getId());
        bpmProcessDef.setName(bpmDef.getName());
        bpmProcessDef.setDefKey(bpmDef.getKey());
        this.bpmDefNodeHandler.setProcessDefNodes(bpmProcessDef, ((Process) this.repositoryService.getBpmnModel(bpmDef.getActDefId()).getProcesses().get(0)).getFlowElements());
        BpmProcessDefParser.processDefParser(bpmProcessDef, bpmDefSetting);
        bpmProcessDef.setJson(bpmDefSetting);
        return bpmProcessDef;
    }

    public List<BpmNodeDef> getNodesByType(String processDefinitionId, NodeType nodeType) {
        List<BpmNodeDef> list = getProcessDefByDefId(processDefinitionId).getBpmnNodeDefs();
        List<BpmNodeDef> rtnList = new ArrayList<>();
        for (BpmNodeDef nodeDef : list) {
            if (nodeDef.getType().equals(nodeType)) {
                rtnList.add(nodeDef);
            }
        }
        return rtnList;
    }

    public List<BpmNodeDef> getAllNodeDef(String processDefinitionId) {
        List<BpmNodeDef> bpmNodeDefs = getNodeDefs(processDefinitionId);
        List<BpmNodeDef> rtnList = new ArrayList<>();
        getBpmNodeDefs(bpmNodeDefs, rtnList);
        return rtnList;
    }

    private void getBpmNodeDefs(List<BpmNodeDef> bpmNodeDefs, List<BpmNodeDef> rtnList) {
        for (BpmNodeDef def : bpmNodeDefs) {
            rtnList.add(def);
            if (NodeType.SUBPROCESS.equals(def.getType())) {
                BpmProcessDef processDef = ((SubProcessNodeDef) def).getChildBpmProcessDef();
                if (processDef != null) {
                    getBpmNodeDefs(processDef.getBpmnNodeDefs(), rtnList);
                }
            }
        }
    }

    public List<BpmNodeDef> getSignUserNode(String processDefinitionId) {
        List<BpmNodeDef> bpmNodeDefs = getAllNodeDef(processDefinitionId);
        List<BpmNodeDef> rtnList = new ArrayList<>();
        for (BpmNodeDef bnd : bpmNodeDefs) {
            if (bnd.getType().equals(NodeType.START) || bnd.getType().equals(NodeType.SIGNTASK) || bnd.getType().equals(NodeType.USERTASK)) {
                rtnList.add(bnd);
            }
        }
        return rtnList;
    }

    public IBpmDefinition getDefinitionById(String defId) {
        return (IBpmDefinition) this.bpmDefinitionManager.get(defId);
    }

    public BpmProcessDef initBpmProcessDef(IBpmDefinition bpmDef) {
        try {
            DefaultBpmProcessDef def = initBpmProcessDef((BpmDefinition) bpmDef);
            BpmContext.cleanTread();
            this.iCache.delByKey("procdef_" + bpmDef.getId());
            return def;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), BpmStatusCode.PARSER_FLOW_ERROR, e);
        }
    }

    public IBpmDefinition getDefinitionByActDefId(String actDefId) {
        return this.bpmDefinitionManager.getDefinitionByActDefId(actDefId);
    }
}
