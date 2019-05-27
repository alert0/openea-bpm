package org.openbpm.bpm.core.manager.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.bpm.api.constant.OpinionStatus;
import org.openbpm.bpm.api.engine.action.cmd.InstanceActionCmd;
import org.openbpm.bpm.api.engine.action.cmd.TaskActionCmd;
import org.openbpm.bpm.api.model.inst.IBpmInstance;
import org.openbpm.bpm.api.model.task.IBpmTask;
import org.openbpm.bpm.core.dao.BpmTaskOpinionDao;
import org.openbpm.bpm.core.manager.BpmTaskOpinionManager;
import org.openbpm.bpm.core.model.BpmTaskOpinion;
import org.openbpm.bpm.engine.action.cmd.DefaultInstanceActionCmd;
import org.openbpm.org.api.model.IUser;
import org.openbpm.sys.api.model.SysIdentity;
import org.openbpm.sys.util.ContextUtil;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.stereotype.Service;

@Service("bpmTaskOpinionManager")
public class BpmTaskOpinionManagerImpl extends BaseManager<String, BpmTaskOpinion> implements BpmTaskOpinionManager {
    @Resource
    BpmTaskOpinionDao bpmTaskOpinionDao;

    public BpmTaskOpinion getByTaskId(String taskId) {
        return this.bpmTaskOpinionDao.getByTaskId(taskId);
    }

    public void createOpinionByInstance(InstanceActionCmd actionModel, boolean isCreateEvent) {
        IBpmInstance bpmInstance = actionModel.getBpmInstance();
        String formIdentity = actionModel.getFormId();
        ExecutionEntity entity = ((DefaultInstanceActionCmd) actionModel).getExecutionEntity();
        entity.getActivityId();
        BpmTaskOpinion bpmTaskOpinion = new BpmTaskOpinion();
        bpmTaskOpinion.setApproveTime(new Date());
        bpmTaskOpinion.setUpdateTime(new Date());
        bpmTaskOpinion.setDurMs(Long.valueOf(0));
        bpmTaskOpinion.setInstId(bpmInstance.getId());
        bpmTaskOpinion.setSupInstId(bpmInstance.getParentInstId());
        bpmTaskOpinion.setOpinion(isCreateEvent ? "发起流程" : "流程结束");
        bpmTaskOpinion.setStatus(isCreateEvent ? "start" : "end");
        bpmTaskOpinion.setTaskId("0");
        bpmTaskOpinion.setTaskKey(entity.getActivityId());
        bpmTaskOpinion.setTaskName(entity.getCurrentActivityName());
        bpmTaskOpinion.setFormId(formIdentity);
        IUser user = ContextUtil.getCurrentUser();
        if (user != null) {
            bpmTaskOpinion.setApprover(user.getUserId());
            bpmTaskOpinion.setApproverName(user.getFullname());
            bpmTaskOpinion.setCreateBy(user.getUserId());
            bpmTaskOpinion.setUpdateBy(user.getUserId());
        }
        create(bpmTaskOpinion);
    }

    public void createOpinionByTask(TaskActionCmd taskActionModel) {
        createOpinion(taskActionModel.getBpmTask(), taskActionModel.getBpmInstance(), taskActionModel.getBpmIdentity(taskActionModel.getNodeId()), taskActionModel.getOpinion(), taskActionModel.getActionName(), taskActionModel.getFormId());
    }

    public void createOpinion(IBpmTask task, IBpmInstance bpmInstance, List<SysIdentity> taskIdentitys, String opinion, String actionName, String formId) {
        createOpinion(task, bpmInstance, taskIdentitys, opinion, actionName, formId, null);
    }

    public void createOpinion(IBpmTask task, IBpmInstance bpmInstance, List<SysIdentity> taskIdentitys, String opinion, String actionName, String formId, String token) {
        BpmTaskOpinion bpmTaskOpinion = new BpmTaskOpinion();
        bpmTaskOpinion.setUpdateTime(new Date());
        bpmTaskOpinion.setDurMs(Long.valueOf(0));
        bpmTaskOpinion.setInstId(bpmInstance.getId());
        bpmTaskOpinion.setSupInstId(bpmInstance.getParentInstId());
        bpmTaskOpinion.setOpinion(opinion);
        bpmTaskOpinion.setStatus(OpinionStatus.getByActionName(actionName).getKey());
        bpmTaskOpinion.setTaskId(task.getId());
        bpmTaskOpinion.setTaskKey(task.getNodeId());
        bpmTaskOpinion.setTaskName(task.getName());
        bpmTaskOpinion.setFormId(formId);
        IUser user = ContextUtil.getCurrentUser();
        if (user != null) {
            bpmTaskOpinion.setCreateBy(user.getUserId());
            bpmTaskOpinion.setUpdateBy(user.getUserId());
        }
        StringBuilder assignInfo = new StringBuilder();
        if (CollectionUtil.isNotEmpty(taskIdentitys)) {
            for (SysIdentity identity : taskIdentitys) {
                assignInfo.append(identity.getType()).append("-").append(identity.getName()).append("-").append(identity.getId()).append(",");
            }
        }
        bpmTaskOpinion.setAssignInfo(assignInfo.toString());
        bpmTaskOpinion.setToken(token);
        create(bpmTaskOpinion);
    }

    public List<BpmTaskOpinion> getByInstAndNode(String instId, String nodeId) {
        return this.bpmTaskOpinionDao.getByInstAndNode(instId, nodeId, null);
    }

    public List<BpmTaskOpinion> getByInstAndToken(String instId, String token) {
        return this.bpmTaskOpinionDao.getByInstAndNode(instId, null, token);
    }

    public List<BpmTaskOpinion> getByInstId(String instId) {
        return this.bpmTaskOpinionDao.getByInstAndNode(instId, null, null);
    }

    public void removeByInstId(String instId) {
        this.bpmTaskOpinionDao.removeByInstId(instId);
    }
}
