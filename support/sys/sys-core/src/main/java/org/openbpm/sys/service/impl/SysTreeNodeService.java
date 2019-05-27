package org.openbpm.sys.service.impl;

import org.openbpm.sys.api.model.ISysTreeNode;
import org.openbpm.sys.api.service.ISysTreeNodeService;
import org.openbpm.sys.core.manager.SysTreeNodeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * 描述：SysTreeNodeService接口
 * </pre>
 */
@Service
public class SysTreeNodeService implements ISysTreeNodeService {
    @Autowired
    SysTreeNodeManager sysTreeNodeManager;

    @Override
    public ISysTreeNode getById(String id) {
        return sysTreeNodeManager.get(id);
    }
}
