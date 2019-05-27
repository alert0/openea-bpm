package org.openbpm.bpm.core.manager.impl;

import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.core.id.IdUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.bpm.act.service.ActInstanceService;
import org.openbpm.bpm.api.model.def.IBpmDefinition;
import org.openbpm.bpm.core.dao.BpmInstanceDao;
import org.openbpm.bpm.core.manager.BpmBusLinkManager;
import org.openbpm.bpm.core.manager.BpmInstanceManager;
import org.openbpm.bpm.core.manager.BpmTaskManager;
import org.openbpm.bpm.core.manager.BpmTaskOpinionManager;
import org.openbpm.bpm.core.manager.BpmTaskStackManager;
import org.openbpm.bpm.core.model.BpmBusLink;
import org.openbpm.bpm.core.model.BpmInstance;
import org.openbpm.bpm.core.model.BpmTaskApprove;
import org.openbpm.bus.api.model.IBusinessObject;
import org.openbpm.bus.api.service.IBusinessDataService;
import org.openbpm.bus.api.service.IBusinessObjectService;
import org.openbpm.org.api.model.IUser;
import org.openbpm.sys.util.ContextUtil;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bpmInstanceManager")
public class BpmInstanceManagerImpl extends BaseManager<String, BpmInstance> implements BpmInstanceManager {
    @Autowired
    ActInstanceService actInstanceService;
    @Autowired
    BpmBusLinkManager bpmBusLinkManager;
    @Resource
    BpmInstanceDao bpmInstanceDao;
    @Autowired
    BpmTaskManager bpmTaskManager;
    @Autowired
    BpmTaskOpinionManager bpmTaskOpinionManager;
    @Autowired
    BpmTaskStackManager bpmTaskStackManager;
    @Autowired
    IBusinessDataService businessDataService;
    @Autowired
    IBusinessObjectService businessObjectService;

    public boolean isSuspendByInstId(String instId) {
        return false;
    }

    public List<BpmInstance> getApplyList(String userId, QueryFilter queryFilter) {
        queryFilter.addParamsFilter("userId", userId);
        return this.bpmInstanceDao.getApplyList(queryFilter);
    }

    public List<BpmTaskApprove> getApproveHistoryList(String userId, QueryFilter queryFilter) {
        queryFilter.addParamsFilter("approver", userId);
        return this.bpmInstanceDao.getApproveHistoryList(queryFilter);
    }

    public BpmInstance getTopInstance(BpmInstance instance) {
        if (instance == null || StringUtil.isZeroEmpty(instance.getParentInstId())) {
            return null;
        }
        BpmInstance parentInstance = (BpmInstance) get(instance.getParentInstId());
        BpmInstance topInstance = getTopInstance(parentInstance);
        if (topInstance == null) {
            return parentInstance;
        }
        return topInstance;
    }

    public BpmInstance genInstanceByDefinition(IBpmDefinition bpmDefinition) {
        BpmInstance instance = new BpmInstance();
        instance.setId(IdUtil.getSuid());
        instance.setSubject(bpmDefinition.getName());
        instance.setDefId(bpmDefinition.getId());
        instance.setTypeId(bpmDefinition.getTypeId());
        instance.setDefKey(bpmDefinition.getKey());
        instance.setActDefId(bpmDefinition.getActDefId());
        instance.setDefName(bpmDefinition.getName());
        IUser user = ContextUtil.getCurrentUser();
        instance.setCreateBy(user.getUserId());
        instance.setCreator(user.getFullname());
        instance.setCreateTime(new Date());
        instance.setSupportMobile(bpmDefinition.getSupportMobile());
        instance.setParentInstId("0");
        if (bpmDefinition.getStatus().equals("deploy")) {
            instance.setIsFormmal("Y");
        } else if (bpmDefinition.getStatus().equals("draft")) {
            instance.setIsFormmal("N");
        }
        instance.setHasCreate(Boolean.valueOf(false));
        return instance;
    }

    public List<BpmInstance> getByPId(String parentInstId) {
        return this.bpmInstanceDao.getByPId(parentInstId);
    }

    public BpmInstance getByActInstId(String actInstId) {
        return this.bpmInstanceDao.getByActInstId(actInstId);
    }

    public void delete(String instId) {
        BpmInstance inst = (BpmInstance) get(instId);
        BpmInstance topInst = getTopInstance(inst);
        if (topInst != null) {
            delete(topInst);
        } else {
            delete(inst);
        }
    }

    private void delete(BpmInstance instance) {
        String instId = instance.getId();
        remove(instId);
        this.bpmTaskManager.removeByInstId(instId);
        this.bpmTaskOpinionManager.removeByInstId(instId);
        this.bpmTaskStackManager.removeByInstanceId(instId);
        for (BpmBusLink link : this.bpmBusLinkManager.getByInstanceId(instId)) {
            IBusinessObject bo = this.businessObjectService.getFilledByKey(link.getBizCode());
            if (bo != null) {
                this.businessDataService.removeData(bo, link.getBizId());
                this.bpmBusLinkManager.remove(link.getId());
            }
        }
        if (StringUtil.isNotEmpty(instance.getActInstId()) && this.actInstanceService.getProcessInstance(instance.getActInstId()) != null) {
            this.actInstanceService.deleteProcessInstance(instance.getActInstId(), String.format("用户：%s[%s]执行删除实例", new Object[]{ContextUtil.getCurrentUser().getFullname(), ContextUtil.getCurrentUser().getAccount()}));
        }
        for (BpmInstance subInst : getByPId(instId)) {
            delete(subInst);
        }
    }
}
