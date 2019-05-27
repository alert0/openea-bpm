package org.openbpm.bpm.api.model.nodedef;

import org.openbpm.bpm.api.constant.NodeType;
import org.openbpm.bpm.api.engine.plugin.context.BpmPluginContext;
import org.openbpm.bpm.api.engine.plugin.def.BpmDef;
import org.openbpm.bpm.api.model.def.BpmProcessDef;
import org.openbpm.bpm.api.model.def.NodeProperties;
import org.openbpm.bpm.api.model.form.BpmForm;
import java.io.Serializable;
import java.util.List;

public interface BpmNodeDef extends Serializable, BpmDef {
    void addIncomeNode(BpmNodeDef bpmNodeDef);

    void addOutcomeNode(BpmNodeDef bpmNodeDef);

    String getAttribute(String str);

    List<BpmPluginContext> getBpmPluginContexts();

    BpmProcessDef getBpmProcessDef();

    List<Button> getButtons();

    BpmForm getForm();

    List<BpmNodeDef> getIncomeNodes();

    List<BpmNodeDef> getInnerOutcomeTaskNodes(boolean z);

    BpmForm getMobileForm();

    String getName();

    String getNodeId();

    NodeProperties getNodeProperties();

    List<BpmNodeDef> getOutcomeNodes();

    List<BpmNodeDef> getOutcomeTaskNodes();

    BpmNodeDef getParentBpmNodeDef();

    <T> T getPluginContext(Class<T> cls);

    String getRealPath();

    BpmProcessDef getRootProcessDef();

    NodeType getType();

    void setType(NodeType nodeType);
}
