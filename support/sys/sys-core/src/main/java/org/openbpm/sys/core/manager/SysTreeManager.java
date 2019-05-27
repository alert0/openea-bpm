package org.openbpm.sys.core.manager;

import org.openbpm.base.manager.Manager;
import org.openbpm.sys.core.model.SysTree;

/**
 * 系统树 Manager处理接口
 *
 */
public interface SysTreeManager extends Manager<String, SysTree> {
    /**
     * <pre>
     * 根据别名获取对象
     * </pre>
     *
     * @param key
     * @return
     */
    SysTree getByKey(String key);

    void removeContainNode(String id);
}
