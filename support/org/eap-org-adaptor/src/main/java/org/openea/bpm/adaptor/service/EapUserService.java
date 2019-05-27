package org.openea.bpm.adaptor.service;

import org.openbpm.org.api.model.IUser;
import org.openbpm.org.api.model.IUserRole;
import org.openbpm.org.api.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service(value = "eapUserService")
public class EapUserService implements UserService {


    /**
     * 根据用户ID获取用户的对象。
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public IUser getUserById(String userId) {
        return null;
    }

    /**
     * 根据用户帐号获取用户对象。
     *
     * @param account
     * @return
     */
    @Override
    public IUser getUserByAccount(String account) {
        return null;
    }

    /**
     * 根据组织id和组织类型获取用户列表。
     *
     * @param groupType 组织类型
     * @param groupId   组织列表
     * @return
     */
    @Override
    public List<? extends IUser> getUserListByGroup(String groupType, String groupId) {
        return null;
    }

    /**
     * 获取用户的角色关系
     *
     * @param userId
     * @return
     */
    @Override
    public List<? extends IUserRole> getUserRole(String userId) {
        return null;
    }
}
