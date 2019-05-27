package org.openbpm.bpm.core.manager;

import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.manager.Manager;
import org.openbpm.bpm.core.model.BpmTask;
import org.openbpm.sys.api.model.SysIdentity;
import java.util.List;

public interface BpmTaskManager extends Manager<String, BpmTask> {
    void assigneeTask(String str, String str2, String str3);

    List<SysIdentity> getAssignUserById(BpmTask bpmTask);

    List<BpmTask> getByInstId(String str);

    List<BpmTask> getByInstIdNodeId(String str, String str2);

    List getTodoList(QueryFilter queryFilter);

    List<BpmTask> getTodoList(String str, QueryFilter queryFilter);

    void removeByInstId(String str);

    void unLockTask(String str);
}
