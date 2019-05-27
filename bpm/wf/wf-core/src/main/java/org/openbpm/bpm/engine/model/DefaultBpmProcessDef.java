package org.openbpm.bpm.engine.model;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.constant.NodeType;
import org.openbpm.bpm.api.engine.plugin.context.BpmPluginContext;
import org.openbpm.bpm.api.model.def.BpmDataModel;
import org.openbpm.bpm.api.model.def.BpmDefProperties;
import org.openbpm.bpm.api.model.def.BpmProcessDef;
import org.openbpm.bpm.api.model.def.BpmVariableDef;
import org.openbpm.bpm.api.model.def.NodeInit;
import org.openbpm.bpm.api.model.form.BpmForm;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class DefaultBpmProcessDef implements BpmProcessDef {
    private static final long serialVersionUID = 1;
    private List<BpmNodeDef> bpmnNodeDefs;
    private List<BpmDataModel> dataModelList = new ArrayList();
    private String defKey = "";
    private BpmDefProperties extPropertys = new BpmDefProperties();
    private BpmForm globalForm = null;
    private BpmForm globalMobileForm = null;
    private BpmForm instForm = null;
    private BpmForm instMobileForm = null;
    private String name = "";
    private List<NodeInit> nodeInitList = new ArrayList();
    private BpmProcessDef parentProcessDef = null;
    private List<BpmPluginContext> pluginContextList = new ArrayList();
    private JSONObject processDefJson;
    private String processDefinitionId = "";
    private List<BpmVariableDef> varList = new ArrayList();


    public BpmDefProperties getExtProperties() {
        return this.extPropertys;
    }

    public void setExtProperties(BpmDefProperties extPropertys2) {
        this.extPropertys = extPropertys2;
    }

    public List<BpmPluginContext> getBpmPluginContexts() {
        return this.pluginContextList;
    }

    public BpmPluginContext getBpmPluginContext(Class<?> clazz) {
        List<BpmPluginContext> Plugins = getBpmPluginContexts();
        if (CollectionUtil.isEmpty(Plugins)) {
            return null;
        }
        for (BpmPluginContext pulgin : Plugins) {
            if (pulgin.getClass().isAssignableFrom(clazz)) {
                return pulgin;
            }
        }
        return null;
    }

    public void setPluginContextList(List<BpmPluginContext> pluginContextList2) {
        Collections.sort(pluginContextList2);
        this.pluginContextList = pluginContextList2;
    }



    public BpmNodeDef getBpmnNodeDef(String nodeId) {
        for (BpmNodeDef nodeDef : this.bpmnNodeDefs) {
            if (nodeId.equals(nodeDef.getNodeId())) {
                return nodeDef;
            }
        }
        return null;
    }

    public BpmNodeDef getStartEvent() {
        for (BpmNodeDef nodeDef : this.bpmnNodeDefs) {
            if (nodeDef.getType().equals(NodeType.START)) {
                return nodeDef;
            }
        }
        return null;
    }

    public List<NodeInit> getNodeInitList() {
        return this.nodeInitList;
    }

    public List<NodeInit> getNodeInitList(String nodeId) {
        List<NodeInit> initList = new ArrayList<>();
        for (NodeInit init : this.nodeInitList) {
            if (StringUtil.isNotEmpty(nodeId) && init.getNodeId().equals(nodeId)) {
                initList.add(init);
            }
        }
        return initList;
    }


    public List<BpmNodeDef> getStartNodes() {
        BpmNodeDef startNode = getStartEvent();
        if (startNode == null) {
            return null;
        }
        return startNode.getOutcomeNodes();
    }

    public List<BpmNodeDef> getEndEvents() {
        List<BpmNodeDef> rtnList = new ArrayList<>();
        for (BpmNodeDef nodeDef : this.bpmnNodeDefs) {
            if (nodeDef.getType().equals(NodeType.END)) {
                rtnList.add(nodeDef);
            }
        }
        return rtnList;
    }

    public String getDataModelKeys() {
        List<String> keys = new ArrayList<>();
        for (BpmDataModel model : this.dataModelList) {
            keys.add(model.getCode());
        }
        return StringUtil.join(keys);
    }

    public void setJson(JSONObject bpmDefSetting) {
        this.processDefJson = bpmDefSetting;
    }

    public JSONObject getJson() {
        return this.processDefJson;
    }

    public List<BpmVariableDef> getVariableList() {
        return this.varList;
    }
}
