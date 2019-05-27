package org.openbpm.bpm.core.manager;

import org.openbpm.base.manager.Manager;
import org.openbpm.bpm.api.model.inst.BpmExecutionStack;
import org.openbpm.bpm.api.model.task.IBpmTask;
import org.openbpm.bpm.core.model.BpmTaskStack;
import java.util.List;

public interface BpmTaskStackManager extends Manager<String, BpmTaskStack> {
    BpmTaskStack createStackByTask(IBpmTask iBpmTask, BpmExecutionStack bpmExecutionStack);

    List<BpmTaskStack> getByInstanceId(String str);

    BpmTaskStack getByTaskId(String str);

    void removeByInstanceId(String str);
}
