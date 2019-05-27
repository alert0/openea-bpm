package org.openbpm.bpm.core.manager.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.id.IdUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.model.task.IBpmTask;
import org.openbpm.bpm.core.dao.TaskIdentityLinkDao;
import org.openbpm.bpm.core.manager.TaskIdentityLinkManager;
import org.openbpm.bpm.core.model.TaskIdentityLink;
import org.openbpm.org.api.model.IGroup;
import org.openbpm.org.api.service.GroupService;
import org.openbpm.sys.api.model.SysIdentity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("taskIdentityLinkManager")
public class TaskIdentityLinkManagerImpl extends BaseManager<String, TaskIdentityLink> implements TaskIdentityLinkManager {
    @Resource
    GroupService groupService;
    @Resource
    TaskIdentityLinkDao taskIdentityLinkDao;

    public void removeByTaskId(String taskId) {
        if (this.taskIdentityLinkDao.queryTaskIdentityCount(taskId) != 0) {
            this.taskIdentityLinkDao.removeByTaskId(taskId);
        }
    }

    public void removeByInstId(String instId) {
        if (this.taskIdentityLinkDao.queryInstanceIdentityCount(instId) != 0) {
            this.taskIdentityLinkDao.removeByInstId(instId);
        }
    }

    public void bulkCreate(List<TaskIdentityLink> list) {
        this.taskIdentityLinkDao.bulkCreate(list);
    }

    public Boolean checkUserOperatorPermission(String userId, String instanceId, String taskId) {
        if (!StringUtil.isEmpty(instanceId) || !StringUtil.isEmpty(taskId)) {
            return Boolean.valueOf(this.taskIdentityLinkDao.checkUserOperatorPermission(getUserRights(userId), taskId, instanceId) > 0);
        }
        throw new BusinessException("检查权限必须提供流程或者任务id", BpmStatusCode.PARAM_ILLEGAL);
    }

    public Set<String> getUserRights(String userId) {
        List<IGroup> list = (List<IGroup>) this.groupService.getGroupsByUserId(userId);
        Set<String> rights = new HashSet<>();
        rights.add(String.format("%s-%s", new Object[]{userId, TaskIdentityLink.RIGHT_TYPE_USER}));
        if (!CollectionUtil.isEmpty(list)) {
            for (IGroup group : list) {
                rights.add(String.format("%s-%s", new Object[]{group.getGroupId(), group.getGroupType()}));
            }
        }
        return rights;
    }

    public void createIdentityLink(IBpmTask bpmTask, List<SysIdentity> identitys) {
        List<TaskIdentityLink> identityLinks = new ArrayList<>();
        for (SysIdentity identity : identitys) {
            TaskIdentityLink link = new TaskIdentityLink();
            link.setId(IdUtil.getSuid());
            link.setIdentity(identity.getId());
            link.setIdentityName(identity.getName());
            link.setType(identity.getType());
            link.setPermissionCode(identity.getId() + "-" + identity.getType());
            link.setTaskId(bpmTask.getId());
            link.setInstId(bpmTask.getInstId());
            identityLinks.add(link);
        }
        bulkCreate(identityLinks);
    }

    public List<TaskIdentityLink> getByTaskId(String taskId) {
        return this.taskIdentityLinkDao.getByTaskId(taskId);
    }
}
