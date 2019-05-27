package org.openbpm.sys.api.service;

import org.openbpm.sys.api.model.ISysTreeNode;

/**
 * <pre>
 * 描述：SysTreeNodeService接口
 * </pre>
 */
public interface ISysTreeNodeService {
    /**
     * <pre>
     * 根据id获取对象
     * </pre>
     *
     * @param id
     * @return
     */
    ISysTreeNode getById(String id);

}
