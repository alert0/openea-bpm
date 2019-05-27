package org.openbpm.bpm.act.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;

public interface ActEventListener {
    void notify(ActivitiEvent activitiEvent);
}
