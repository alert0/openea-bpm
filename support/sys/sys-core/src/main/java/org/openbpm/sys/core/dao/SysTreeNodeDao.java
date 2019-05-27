package org.openbpm.sys.core.dao;

import org.mybatis.spring.annotation.MapperScan;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.sys.core.model.SysTreeNode;

/**
 * 系统树节点 DAO接口
 *
 */
@MapperScan
public interface SysTreeNodeDao extends BaseDao<String, SysTreeNode> {

    /**
     * <pre>
     * 根据树id删除节点
     * </pre>
     *
     * @param treeId
     */
    void removeByTreeId(String treeId);
    
    /**
     * <pre>
     * 删除path下的全部节点
     * </pre>
     *
     * @param path
     */
    void removeByPath(String path);

}
