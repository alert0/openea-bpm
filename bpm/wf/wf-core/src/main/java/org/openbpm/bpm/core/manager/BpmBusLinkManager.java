package org.openbpm.bpm.core.manager;

import org.openbpm.base.manager.Manager;
import org.openbpm.bpm.core.model.BpmBusLink;
import java.util.List;

public interface BpmBusLinkManager extends Manager<String, BpmBusLink> {
    List<BpmBusLink> getByInstanceId(String str);
}
