package org.openbpm.bpm.core.manager;

import org.openbpm.base.manager.Manager;
import org.openbpm.bpm.api.model.task.IBpmTask;
import org.openbpm.bpm.core.model.TaskIdentityLink;
import org.openbpm.sys.api.model.SysIdentity;
import java.util.List;
import java.util.Set;

public interface TaskIdentityLinkManager extends Manager<String, TaskIdentityLink> {
    void bulkCreate(List<TaskIdentityLink> list);

    Boolean checkUserOperatorPermission(String str, String str2, String str3);

    void createIdentityLink(IBpmTask iBpmTask, List<SysIdentity> list);

    List<TaskIdentityLink> getByTaskId(String str);

    Set<String> getUserRights(String str);

    void removeByInstId(String str);

    void removeByTaskId(String str);
}
