package org.openbpm.bpm.core.listener;

import org.openbpm.bpm.act.cache.ActivitiDefCache;
import org.openbpm.bpm.api.engine.event.BpmDefinitionUpdateEvent;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.bpm.core.model.BpmDefinition;
import javax.annotation.Resource;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class BpmDefinitionUpdateEventListener implements ApplicationListener<BpmDefinitionUpdateEvent> {
    @Resource
    BpmProcessDefService bpmProcessDefService;

    public void onApplicationEvent(BpmDefinitionUpdateEvent event) {
        BpmDefinition bpmDef = (BpmDefinition) event.getSource();
        this.bpmProcessDefService.clean(bpmDef.getId());
        ActivitiDefCache.clearByDefId(bpmDef.getActDefId());
    }
}
