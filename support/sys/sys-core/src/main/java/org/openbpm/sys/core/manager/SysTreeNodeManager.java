package org.openbpm.sys.core.manager;

import java.util.List;

import org.openbpm.base.manager.Manager;
import org.openbpm.sys.core.model.SysTreeNode;

/**
 * 系统树节点 Manager处理接口
 *
 */
public interface SysTreeNodeManager extends Manager<String, SysTreeNode> {

    /**
     * <pre>
     * 根据树id获取节点
     * 根据sn字段升序
     * </pre>
     *
     * @param treeId
     * @return
     */
    List<SysTreeNode> getByTreeId(String treeId);

    /**
     * <pre>
     * 获取指定树下的指定节点
     * </pre>
     *
     * @param treeId
     * @param key
     * @return
     */
    SysTreeNode getByTreeIdAndKey(String treeId, String key);

    /**
     * <pre>
     * 根据父节点获取其子节点
     * 不会进行递归查询，只获取第一层
     * </pre>
     *
     * @param parentId
     * @return
     */
    List<SysTreeNode> getByParentId(String parentId);

    /**
     * <pre>
     * 获取以path开始的路径
     * </pre>
     *
     * @param path
     * @return
     */
    List<SysTreeNode> getStartWithPath(String path);

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
