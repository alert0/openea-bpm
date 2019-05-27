package org.openbpm.bpm.core.manager;

import org.openbpm.base.manager.Manager;
import org.openbpm.bpm.api.engine.action.cmd.InstanceActionCmd;
import org.openbpm.bpm.api.engine.action.cmd.TaskActionCmd;
import org.openbpm.bpm.api.model.inst.IBpmInstance;
import org.openbpm.bpm.api.model.task.IBpmTask;
import org.openbpm.bpm.core.model.BpmTaskOpinion;
import org.openbpm.sys.api.model.SysIdentity;
import java.util.List;

public interface BpmTaskOpinionManager extends Manager<String, BpmTaskOpinion> {
    void createOpinion(IBpmTask iBpmTask, IBpmInstance iBpmInstance, List<SysIdentity> list, String str, String str2, String str3);

    void createOpinion(IBpmTask iBpmTask, IBpmInstance iBpmInstance, List<SysIdentity> list, String str, String str2, String str3, String str4);

    void createOpinionByInstance(InstanceActionCmd instanceActionCmd, boolean z);

    void createOpinionByTask(TaskActionCmd taskActionCmd);

    List<BpmTaskOpinion> getByInstAndNode(String str, String str2);

    List<BpmTaskOpinion> getByInstAndToken(String str, String str2);

    List<BpmTaskOpinion> getByInstId(String str);

    BpmTaskOpinion getByTaskId(String str);

    void removeByInstId(String str);
}
