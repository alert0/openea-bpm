package org.openbpm.bpm.plugin.core.manager.impl;

import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.bpm.plugin.core.dao.BpmSubmitDataLogDao;
import org.openbpm.bpm.plugin.core.manager.BpmSubmitDataLogManager;
import org.openbpm.bpm.plugin.core.model.BpmSubmitDataLog;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("bpmSubmitDataLogManager")
public class BpmSubmitDataLogManagerImpl extends BaseManager<String, BpmSubmitDataLog> implements BpmSubmitDataLogManager {
    @Resource
    BpmSubmitDataLogDao bpmSubmitDataLogDao;
}
