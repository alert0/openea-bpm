package org.openbpm.sys.core.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.openbpm.sys.core.dao.SysTreeNodeDao;
import org.openbpm.sys.core.manager.SysTreeNodeManager;
import org.openbpm.sys.core.model.SysTreeNode;
import org.springframework.stereotype.Service;

import org.openbpm.base.api.constant.Direction;
import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.api.query.QueryOP;
import org.openbpm.base.db.model.query.DefaultQueryFilter;
import org.openbpm.base.manager.impl.BaseManager;

/**
 * 系统树节点 Manager处理实现类
 *
 */
@Service("sysTreeNodeManager")
public class SysTreeNodeManagerImpl extends BaseManager<String, SysTreeNode> implements SysTreeNodeManager {
    @Resource
    SysTreeNodeDao sysTreeNodeDao;

    @Override
    public List<SysTreeNode> getByTreeId(String treeId) {
        QueryFilter filter = new DefaultQueryFilter();
        filter.addFilter("tree_id_", treeId, QueryOP.EQUAL);
        filter.addFieldSort("sn_", Direction.ASC.getKey());
        return this.query(filter);
    }

    @Override
    public SysTreeNode getByTreeIdAndKey(String treeId, String key) {
        QueryFilter filter = new DefaultQueryFilter();
        filter.addFilter("tree_id_", treeId, QueryOP.EQUAL);
        filter.addFilter("key_", key, QueryOP.EQUAL);
        return this.queryOne(filter);
    }

    @Override
    public List<SysTreeNode> getByParentId(String parentId) {
        QueryFilter filter = new DefaultQueryFilter();
        filter.addFilter("parent_id_", parentId, QueryOP.EQUAL);
        return this.query(filter);
    }

    @Override
    public List<SysTreeNode> getStartWithPath(String path) {
        QueryFilter filter = new DefaultQueryFilter();
        filter.addFilter("path_", path , QueryOP.RIGHT_LIKE);
        return this.query(filter);
    }

    @Override
    public void removeByTreeId(String treeId) {
        sysTreeNodeDao.removeByTreeId(treeId);
    }
    
    @Override
    public void removeByPath(String path) {
    	sysTreeNodeDao.removeByPath(path);
    }
}
