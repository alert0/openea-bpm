package org.openbpm.bpm.api.model.def;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.bpm.api.engine.plugin.def.BpmDef;
import org.openbpm.bpm.api.model.form.BpmForm;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import java.util.List;

public interface BpmProcessDef extends BpmDef {
    BpmNodeDef getBpmnNodeDef(String str);

    List<BpmNodeDef> getBpmnNodeDefs();

    String getDefKey();

    List<BpmNodeDef> getEndEvents();

    String getName();

    BpmProcessDef getParentProcessDef();

    String getProcessDefinitionId();

    BpmNodeDef getStartEvent();

    List<BpmNodeDef> getStartNodes();

    JSONObject getJson();

    List<BpmDataModel> getDataModelList();

    BpmDefProperties getExtProperties();

    List<NodeInit> getNodeInitList(String nodeId);

    BpmForm getGlobalForm();
}
