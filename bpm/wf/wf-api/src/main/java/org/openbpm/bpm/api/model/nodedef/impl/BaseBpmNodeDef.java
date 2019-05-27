package org.openbpm.bpm.api.model.nodedef.impl;

import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.bpm.api.constant.NodeType;
import org.openbpm.bpm.api.engine.action.button.ButtonFactory;
import org.openbpm.bpm.api.engine.plugin.context.BpmPluginContext;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.model.def.BpmProcessDef;
import org.openbpm.bpm.api.model.def.NodeInit;
import org.openbpm.bpm.api.model.def.NodeProperties;
import org.openbpm.bpm.api.model.form.BpmForm;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.api.model.nodedef.Button;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseBpmNodeDef implements BpmNodeDef {
    private static final long serialVersionUID = -2044846605763777657L;
    private Map<String, String> attrMap = new HashMap();
    private List<BpmPluginContext> bpmPluginContexts = new ArrayList();
    private BpmProcessDef bpmProcessDef;
    private List<Button> buttonList = null;
    private List<Button> buttons = null;
    private BpmForm form;
    private List<BpmNodeDef> incomeNodes = new ArrayList();
    private BpmForm mobileForm;
    private String name;
    private String nodeId;
    private List<NodeInit> nodeInits = new ArrayList();
    private NodeProperties nodeProperties = new NodeProperties();
    private List<BpmNodeDef> outcomeNodes = new ArrayList();
    private BpmNodeDef parentBpmNodeDef;
    private NodeType type;

    public String getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(String nodeId2) {
        this.nodeId = nodeId2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public NodeType getType() {
        return this.type;
    }

    public void setType(NodeType type2) {
        this.type = type2;
    }

    public List<BpmNodeDef> getIncomeNodes() {
        return this.incomeNodes;
    }

    public void setIncomeNodes(List<BpmNodeDef> incomeNodes2) {
        this.incomeNodes = incomeNodes2;
    }

    public List<BpmNodeDef> getOutcomeNodes() {
        return this.outcomeNodes;
    }

    public void setOutcomeNodes(List<BpmNodeDef> outcomeNodes2) {
        this.outcomeNodes = outcomeNodes2;
    }

    public List<BpmPluginContext> getBpmPluginContexts() {
        return this.bpmPluginContexts;
    }

    public void setBpmPluginContexts(List<BpmPluginContext> bpmPluginContexts2) {
        Collections.sort(bpmPluginContexts2);
        this.bpmPluginContexts = bpmPluginContexts2;
    }

    public void setAttribute(String name2, String value) {
        this.attrMap.put(name2.toLowerCase().trim().toLowerCase(), value);
    }

    public String getAttribute(String name2) {
        return (String) this.attrMap.get(name2.toLowerCase().trim());
    }

    public void addIncomeNode(BpmNodeDef bpmNodeDef) {
        this.incomeNodes.add(bpmNodeDef);
    }

    public void addOutcomeNode(BpmNodeDef bpmNodeDef) {
        this.outcomeNodes.add(bpmNodeDef);
    }

    public BpmNodeDef getParentBpmNodeDef() {
        return this.parentBpmNodeDef;
    }

    public void setParentBpmNodeDef(BpmNodeDef parentBpmNodeDef2) {
        this.parentBpmNodeDef = parentBpmNodeDef2;
    }

    public String getRealPath() {
        String id = getNodeId();
        for (BpmNodeDef parent = getParentBpmNodeDef(); parent != null; parent = parent.getParentBpmNodeDef()) {
            id = parent.getNodeId() + "/" + id;
        }
        return id;
    }

    public BpmProcessDef getBpmProcessDef() {
        return this.bpmProcessDef;
    }

    public void setBpmProcessDef(BpmProcessDef bpmProcessDef2) {
        this.bpmProcessDef = bpmProcessDef2;
    }

    public BpmProcessDef getRootProcessDef() {
        BpmProcessDef processDef = this.bpmProcessDef;
        for (BpmProcessDef parent = processDef.getParentProcessDef(); parent != null; parent = parent.getParentProcessDef()) {
            processDef = parent;
        }
        return processDef;
    }

    public List<BpmNodeDef> getOutcomeTaskNodes() {
        return getNodeDefList(this.outcomeNodes);
    }

    private List<BpmNodeDef> getNodeDefList(List<BpmNodeDef> bpmNodeDefs) {
        List<BpmNodeDef> bpmNodeList = new ArrayList<>();
        for (BpmNodeDef def : bpmNodeDefs) {
            if (NodeType.USERTASK.equals(def.getType()) || NodeType.SIGNTASK.equals(def.getType())) {
                bpmNodeList.add(def);
            } else if (NodeType.SUBPROCESS.equals(def.getType())) {
                bpmNodeList.addAll(getNodeDefList(((SubProcessNodeDef) def).getChildBpmProcessDef().getStartEvent().getOutcomeNodes()));
            } else if (!NodeType.END.equals(def.getType()) || def.getParentBpmNodeDef() == null || !NodeType.SUBPROCESS.equals(def.getParentBpmNodeDef().getType())) {
                bpmNodeList.addAll(getNodeDefList(def.getOutcomeNodes()));
            } else {
                bpmNodeList.addAll(getNodeDefList(((SubProcessNodeDef) def.getParentBpmNodeDef()).getOutcomeNodes()));
            }
        }
        return bpmNodeList;
    }

    public List<BpmNodeDef> getInnerOutcomeTaskNodes(boolean includeSignTask) {
        return getInnerOutcomeTaskNodes(this.outcomeNodes, includeSignTask);
    }

    private List<BpmNodeDef> getInnerOutcomeTaskNodes(List<BpmNodeDef> bpmNodeDefs, boolean includeSignTask) {
        List<BpmNodeDef> bpmNodeList = new ArrayList<>();
        for (BpmNodeDef def : bpmNodeDefs) {
            if (NodeType.USERTASK.equals(def.getType()) || (includeSignTask && NodeType.SIGNTASK.equals(def.getType()))) {
                bpmNodeList.add(def);
            } else if (!NodeType.SUBPROCESS.equals(def.getType()) && !NodeType.CALLACTIVITY.equals(def.getType()) && !NodeType.END.equals(def.getType())) {
                bpmNodeList.addAll(getInnerOutcomeTaskNodes(def.getOutcomeNodes(), includeSignTask));
            }
        }
        return bpmNodeList;
    }

    public <T> T getPluginContext(Class<T> cls) {
        for (BpmPluginContext context : this.bpmPluginContexts) {
            if (context.getClass().isAssignableFrom(cls)) {
                return (T)context;
            }
        }
        return null;
    }

    public NodeProperties getNodeProperties() {
        return this.nodeProperties;
    }

    public void setNodeProperties(NodeProperties nodeProperties2) {
        this.nodeProperties = nodeProperties2;
    }

    public void setButtons(List<Button> buttons2) {
        this.buttons = buttons2;
    }

    public List<Button> getButtons() {
        if (this.buttons != null) {
            return this.buttons;
        }
        if (this.buttonList != null) {
            return this.buttonList;
        }
        try {
            this.buttonList = ButtonFactory.generateButtons(this, true);
            return this.buttonList;
        } catch (Exception e) {
            throw new BusinessException(BpmStatusCode.TASK_ACTION_BTN_ERROR, e);
        }
    }

    public boolean isDefaultBtn() {
        if (this.buttons != null) {
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(":").append(getNodeId()).append(super.toString());
        return sb.toString();
    }

    public BpmForm getMobileForm() {
        return this.mobileForm;
    }

    public void setMobileForm(BpmForm mobileForm2) {
        this.mobileForm = mobileForm2;
    }

    public BpmForm getForm() {
        return this.form;
    }

    public void setForm(BpmForm form2) {
        this.form = form2;
    }
}
