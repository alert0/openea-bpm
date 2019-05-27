package org.openbpm.bpm.api.service;

import org.openbpm.bpm.api.constant.NodeType;
import org.openbpm.bpm.api.model.def.BpmProcessDef;
import org.openbpm.bpm.api.model.def.IBpmDefinition;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import java.util.List;

public interface BpmProcessDefService {
    void clean(String str);

    List<BpmNodeDef> getAllNodeDef(String str);

    BpmNodeDef getBpmNodeDef(String str, String str2);

    BpmProcessDef getBpmProcessDef(String str);

    IBpmDefinition getDefinitionByActDefId(String str);

    IBpmDefinition getDefinitionById(String str);

    List<BpmNodeDef> getEndEvents(String str);

    List<BpmNodeDef> getNodeDefs(String str);

    List<BpmNodeDef> getNodesByType(String str, NodeType nodeType);

    List<BpmNodeDef> getSignUserNode(String str);

    BpmNodeDef getStartEvent(String str);

    List<BpmNodeDef> getStartNodes(String str);

    BpmProcessDef initBpmProcessDef(IBpmDefinition iBpmDefinition);

    boolean isContainCallActivity(String str);

    boolean isStartNode(String str, String str2);

    boolean validNodeDefType(String str, String str2, NodeType nodeType);
}
