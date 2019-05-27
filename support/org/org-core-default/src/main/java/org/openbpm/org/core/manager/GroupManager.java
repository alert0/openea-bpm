package org.openbpm.org.core.manager;

import org.openbpm.base.manager.Manager;
import org.openbpm.org.core.model.Group;

import java.util.List;

/**
 * <pre>
 * 描述：组织架构 处理接口
 * </pre>
 */
public interface GroupManager extends Manager<String, Group> {
    /**
     * 根据Code取定义对象。
     *
     * @param code
     * @return
     */
    Group getByCode(String code);

    /**
     * 根据用户ID获取组织列表
     *
     * @param userId
     * @return
     */
    List<Group> getByUserId(String userId);

    /**
     * 获取主组织。
     *
     * @param userId
     * @return
     */
    Group getMainGroup(String userId);
}
