package org.openbpm.sys.core.manager.impl;

import javax.annotation.Resource;

import org.openbpm.sys.core.dao.SysTreeDao;
import org.openbpm.sys.core.manager.SysTreeManager;
import org.openbpm.sys.core.manager.SysTreeNodeManager;
import org.openbpm.sys.core.model.SysTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.api.query.QueryOP;
import org.openbpm.base.db.model.query.DefaultQueryFilter;
import org.openbpm.base.manager.impl.BaseManager;

/**
 * 系统树 Manager处理实现类
 *
 */
@Service("sysTreeManager")
public class SysTreeManagerImpl extends BaseManager<String, SysTree> implements SysTreeManager {
    @Resource
    SysTreeDao sysTreeDao;
    @Autowired
    SysTreeNodeManager sysTreeNodeManager;

    @Override
    public SysTree getByKey(String key) {
        QueryFilter filter = new DefaultQueryFilter();
        filter.addFilter("key_", key, QueryOP.EQUAL);
        return this.queryOne(filter);
    }

    @Override
    public void removeContainNode(String id) {
        this.remove(id);
        sysTreeNodeManager.removeByTreeId(id);
    }
}
