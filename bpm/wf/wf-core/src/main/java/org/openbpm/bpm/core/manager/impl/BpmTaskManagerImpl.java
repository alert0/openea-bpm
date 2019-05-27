package org.openbpm.bpm.core.manager.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.api.exception.BusinessMessage;
import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.api.query.QueryOP;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.bpm.api.constant.TaskStatus;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.core.dao.BpmTaskDao;
import org.openbpm.bpm.core.manager.BpmInstanceManager;
import org.openbpm.bpm.core.manager.BpmTaskManager;
import org.openbpm.bpm.core.manager.TaskIdentityLinkManager;
import org.openbpm.bpm.core.model.BpmTask;
import org.openbpm.bpm.core.model.TaskIdentityLink;
import org.openbpm.bpm.engine.model.BpmIdentity;
import org.openbpm.bpm.engine.parser.FlowConfigConstants.FORM;
import org.openbpm.sys.api.model.SysIdentity;
import org.openbpm.sys.util.ContextUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("bpmTaskManager")
public class BpmTaskManagerImpl extends BaseManager<String, BpmTask> implements BpmTaskManager {
    @Resource
    BpmTaskDao bpmTaskDao;
    @Resource
    BpmInstanceManager instanceManager;
    @Resource
    TaskIdentityLinkManager taskIdentityLinkManager;

    public List<BpmTask> getByInstIdNodeId(String instId, String nodeId) {
        return this.bpmTaskDao.getByInstIdNodeId(instId, nodeId);
    }

    public List<BpmTask> getByInstId(String instId) {
        return this.bpmTaskDao.getByInstIdNodeId(instId, null);
    }

    public void removeByInstId(String instId) {
        this.bpmTaskDao.removeByInstId(instId);
    }

    public List<BpmTask> getTodoList(String userId, QueryFilter queryFilter) {
        queryFilter.addParamsFilter("userRights", this.taskIdentityLinkManager.getUserRights(userId));
        queryFilter.addParamsFilter("userId", ContextUtil.getCurrentUserId());
        return this.bpmTaskDao.getTodoList(queryFilter);
    }

    public List getTodoList(QueryFilter queryFilter) {
        String userId = ContextUtil.getCurrentUserId();
        String type = (String) queryFilter.getParams().get(FORM.TYPE);
        String title = (String) queryFilter.getParams().get("subject");
        if (StringUtil.isNotEmpty(title)) {
            queryFilter.addFilter("subject_", title, QueryOP.LIKE);
        }
        if ("done".equals(type)) {
            return this.instanceManager.getApproveHistoryList(userId, queryFilter);
        }
        queryFilter.addParamsFilter("userRights", this.taskIdentityLinkManager.getUserRights(userId));
        queryFilter.addParamsFilter("userId", userId);
        return this.bpmTaskDao.getTodoList(queryFilter);
    }

    public void assigneeTask(String taskId, String userId, String userName) {
        BpmTask task = (BpmTask) get(taskId);
        if (task == null) {
            throw new BusinessException("任务可能已经被处理，请刷新。", BpmStatusCode.TASK_NOT_FOUND);
        }
        task.setAssigneeId(userId);
        task.setAssigneeNames(userName);
        task.setStatus(TaskStatus.DESIGNATE.getKey());
        update(task);
    }

    public void unLockTask(String taskId) {
        BpmTask task = (BpmTask) get(taskId);
        List<TaskIdentityLink> identitys = this.taskIdentityLinkManager.getByTaskId(task.getId());
        if (CollectionUtil.isEmpty(identitys)) {
            throw new BusinessMessage("该任务并非多候选人状态，无效的操作！");
        }
        List<String> names = new ArrayList<>();
        for (TaskIdentityLink identity : identitys) {
            names.add(identity.getIdentityName());
        }
        task.setAssigneeId("0");
        task.setAssigneeNames(StringUtil.join(names));
        task.setStatus(TaskStatus.NORMAL.getKey());
        update(task);
    }

    public List<SysIdentity> getAssignUserById(BpmTask task) {
        if (!StringUtil.isNotZeroEmpty(task.getAssigneeId()) || "-1".equals(task.getAssigneeId())) {
            List<SysIdentity> identityList = new ArrayList<>();
            for (TaskIdentityLink link : this.taskIdentityLinkManager.getByTaskId(task.getId())) {
                identityList.add(new BpmIdentity(link.getIdentity(), link.getIdentityName(), link.getType()));
            }
            return identityList;
        }
        return Arrays.asList(new SysIdentity[]{new BpmIdentity(task.getAssigneeId(), task.getAssigneeNames(), TaskIdentityLink.RIGHT_TYPE_USER)});
    }
}
