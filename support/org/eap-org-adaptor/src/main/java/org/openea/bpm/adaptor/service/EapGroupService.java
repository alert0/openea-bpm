package org.openea.bpm.adaptor.service;


import org.openbpm.org.api.model.IGroup;
import org.openbpm.org.api.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用户与组关系的实现类：通过用户找组，通过组找人等
 */
@Service("eapGroupService")
public class EapGroupService implements GroupService {


    /**
     * 根据用户ID和组类别获取相关的组。
     *
     * @param groupType 用户组类型
     * @param userId    用户ID
     * @return
     */
    @Override
    public List<? extends IGroup> getGroupsByGroupTypeUserId(String groupType, String userId) {
        return null;
    }

    /**
     * 根据用户账号获取用户当前所在的组。
     *
     * @param account 用户帐号
     * @return 返回一个Map，键为维度类型，值为组列表。
     */
    @Override
    public Map<String, List<? extends IGroup>> getAllGroupByAccount(String account) {
        return null;
    }

    /**
     * 获取用户所在的所有组织。
     *
     * @param userId 用户ID
     * @return 返回一个Map，键为维度类型，值为组列表。
     */
    @Override
    public Map<String, List<? extends IGroup>> getAllGroupByUserId(String userId) {
        return null;
    }

    /**
     * 根据用户获取用户所属的组。
     *
     * @param userId
     * @return
     */
    @Override
    public List<? extends IGroup> getGroupsByUserId(String userId) {
        return null;
    }

    /**
     * 根据组织ID和类型获取组织对象。
     *
     * @param groupType
     * @param groupId
     * @return
     */
    @Override
    public IGroup getById(String groupType, String groupId) {
        return null;
    }

    /**
     * 根据组织ID和类型获取组织对象。
     *
     * @param groupType
     * @param code
     * @return
     */
    @Override
    public IGroup getByCode(String groupType, String code) {
        return null;
    }

    @Override
    public IGroup getMainGroup(String userId) {
        return null;
    }
}
