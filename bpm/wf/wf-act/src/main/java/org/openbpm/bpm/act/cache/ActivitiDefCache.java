package org.openbpm.bpm.act.cache;

import cn.hutool.core.util.ObjectUtil;
import org.openbpm.base.core.cache.ICache;
import org.openbpm.base.core.util.AppUtil;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.activiti.engine.impl.persistence.deploy.DeploymentCache;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.springframework.stereotype.Component;

@Component
public class ActivitiDefCache implements DeploymentCache<ProcessDefinitionEntity> {
    public static ActivitiDefCache activitiDefCache = null;
    private ThreadLocal<Map<String, ProcessDefinitionEntity>> threadLocal = new ThreadLocal<>();
    @Resource
    ICache cache;

    public static void clearLocal() {
        ((ActivitiDefCache) AppUtil.getBean(ActivitiDefCache.class)).clearProcessCache();
    }

    public static void clearByDefId(String actDefId) {
        if (activitiDefCache == null) {
            activitiDefCache = (ActivitiDefCache) AppUtil.getBean(ActivitiDefCache.class);
        }
        activitiDefCache.clearProcessDefinitionEntity(actDefId);
        activitiDefCache.clearProcessCache();
    }

    private void clearProcessDefinitionEntity(String defId) {
        remove(defId);
        this.threadLocal.remove();
    }

    private void clearProcessCache() {
        this.threadLocal.remove();
    }

    private void setThreadLocalDef(ProcessDefinitionEntity processEnt) {
        if (this.threadLocal.get() == null) {
            Map<String, ProcessDefinitionEntity> map = new HashMap<>();
            map.put(processEnt.getId(), processEnt);
            this.threadLocal.set(map);
            return;
        }
        ((Map) this.threadLocal.get()).put(processEnt.getId(), processEnt);
    }

    private ProcessDefinitionEntity getThreadLocalDef(String processDefinitionId) {
        if (this.threadLocal.get() == null) {
            return null;
        }
        Map<String, ProcessDefinitionEntity> map = (Map) this.threadLocal.get();
        if (map.containsKey(processDefinitionId)) {
            return (ProcessDefinitionEntity) map.get(processDefinitionId);
        }
        return null;
    }

    public ProcessDefinitionEntity get(String id) {
        ProcessDefinitionEntity p = getThreadLocalDef(id);
        if (p != null) {
            return p;
        }
        ProcessDefinitionEntity ent = (ProcessDefinitionEntity) this.cache.getByKey(id);
        if (ent == null) {
            return null;
        }
        ProcessDefinitionEntity cloneEnt = null;
        try {
            cloneEnt = (ProcessDefinitionEntity) ObjectUtil.cloneByStream(ent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setThreadLocalDef(cloneEnt);
        return cloneEnt;
    }

    public void add(String id, ProcessDefinitionEntity object) {
        this.cache.add(id, object);
    }

    public void remove(String id) {
        this.cache.delByKey(id);
    }

    public void clear() {
        this.cache.clearAll();
    }
}
