package org.openbpm.bpm.core.manager;

import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.manager.Manager;
import org.openbpm.bpm.api.model.def.IBpmDefinition;
import org.openbpm.bpm.core.model.BpmInstance;
import org.openbpm.bpm.core.model.BpmTaskApprove;
import java.util.List;

public interface BpmInstanceManager extends Manager<String, BpmInstance> {
    void delete(String str);

    BpmInstance genInstanceByDefinition(IBpmDefinition iBpmDefinition);

    List<BpmInstance> getApplyList(String str, QueryFilter queryFilter);

    List<BpmTaskApprove> getApproveHistoryList(String str, QueryFilter queryFilter);

    BpmInstance getByActInstId(String str);

    List<BpmInstance> getByPId(String str);

    BpmInstance getTopInstance(BpmInstance bpmInstance);

    boolean isSuspendByInstId(String str);
}
