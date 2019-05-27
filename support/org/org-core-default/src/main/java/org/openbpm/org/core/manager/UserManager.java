package org.openbpm.org.core.manager;

import java.util.List;

import org.openbpm.base.manager.Manager;
import org.openbpm.org.core.model.User;

/**
 * <pre>
 * 描述：用户表 处理接口
 * </pre>
 */
public interface UserManager extends Manager<String, User> {
    /**
     * 根据Account取定义对象。
     *
     * @param code
     * @return
     */
    User getByAccount(String account);

    /**
     * 根据角色ID获取用户列表
     *
     * @param relId 关系ID
     * @param type 关系类型  post , org , role
     * @return
     */
    List<User> getUserListByRelation(String relId,String type);

    /**
     * 判断系统中用户是否存在。
     */
    boolean isUserExist(User user);

	void saveUserInfo(User user);

}
