package org.openbpm.bpm.core.manager;

import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.manager.Manager;
import org.openbpm.bpm.core.model.BpmDefinition;
import java.util.List;
import java.util.Map;
import org.activiti.engine.repository.Model;

public interface BpmDefinitionManager extends Manager<String, BpmDefinition> {
    String createActModel(BpmDefinition bpmDefinition);

    BpmDefinition getByKey(String str);

    BpmDefinition getDefByActModelId(String str);

    BpmDefinition getDefinitionByActDefId(String str);

    List<BpmDefinition> getMyDefinitionList(String str, QueryFilter queryFilter);

    void updateBpmnModel(Model model, Map<String, String> map) throws Exception;
}
