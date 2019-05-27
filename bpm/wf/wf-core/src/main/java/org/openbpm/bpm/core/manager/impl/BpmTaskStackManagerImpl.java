package org.openbpm.bpm.core.manager.impl;

import org.openbpm.base.core.id.IdUtil;
import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.model.inst.BpmExecutionStack;
import org.openbpm.bpm.api.model.task.IBpmTask;
import org.openbpm.bpm.core.dao.BpmTaskStackDao;
import org.openbpm.bpm.core.manager.BpmTaskStackManager;
import org.openbpm.bpm.core.model.BpmTaskStack;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("bpmExecutionStackManager")
public class BpmTaskStackManagerImpl extends BaseManager<String, BpmTaskStack> implements BpmTaskStackManager {
    @Resource
    BpmTaskStackDao bpmTaskStackDao;

    public BpmTaskStack getByTaskId(String taskId) {
        return this.bpmTaskStackDao.getByTaskId(taskId);
    }
    
    public BpmTaskStack createStackByTask(IBpmTask task, BpmExecutionStack parentStack) {
        BpmTaskStack bpmTaskStack = new BpmTaskStack();
        bpmTaskStack.setId(IdUtil.getSuid());
        bpmTaskStack.setNodeId(task.getNodeId());
        bpmTaskStack.setNodeName(task.getName());
        bpmTaskStack.setTaskId(task.getId());
        bpmTaskStack.setStartTime(new Date());
        bpmTaskStack.setInstId(task.getInstId());
        bpmTaskStack.setNodeType("userTask");
        bpmTaskStack.setActionName(BpmContext.getActionModel());
        if (parentStack == null) {
            bpmTaskStack.setParentId("0");
        } else {
            bpmTaskStack.setParentId(parentStack.getId());
        }
        create(bpmTaskStack);
        return bpmTaskStack;
    }

    @Deprecated
    public void removeByInstanceId(String instId) {
    }

    public List<BpmTaskStack> getByInstanceId(String instId) {
        return this.bpmTaskStackDao.getByInstanceId(instId);
    }
}
