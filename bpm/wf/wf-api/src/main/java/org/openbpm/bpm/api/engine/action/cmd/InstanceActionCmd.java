package org.openbpm.bpm.api.engine.action.cmd;

import org.openbpm.bpm.api.model.inst.IBpmInstance;

public interface InstanceActionCmd extends ActionCmd {
    IBpmInstance getBpmInstance();

    String getBusinessKey();

    String getInstanceId();

    String getSubject();
}
