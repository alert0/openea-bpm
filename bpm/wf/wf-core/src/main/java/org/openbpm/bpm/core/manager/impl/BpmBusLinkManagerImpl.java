package org.openbpm.bpm.core.manager.impl;

import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.bpm.core.dao.BpmBusLinkDao;
import org.openbpm.bpm.core.manager.BpmBusLinkManager;
import org.openbpm.bpm.core.model.BpmBusLink;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("bpmBusLinkManager")
public class BpmBusLinkManagerImpl extends BaseManager<String, BpmBusLink> implements BpmBusLinkManager {
    @Resource
    BpmBusLinkDao bpmBusLinkDao;

    public List<BpmBusLink> getByInstanceId(String instanceId) {
        return this.bpmBusLinkDao.getByInstanceId(instanceId);
    }
}
