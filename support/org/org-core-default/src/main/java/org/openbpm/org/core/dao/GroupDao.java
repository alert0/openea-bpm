package org.openbpm.org.core.dao;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.org.core.model.Group;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;


/**
 * <pre>
 * 描述：组织架构 DAO接口
 * </pre>
 */
@MapperScan 
public interface GroupDao extends BaseDao<String, Group> {
    /**
     * 根据Code取定义对象。
     *
     * @param code
     * @return
     */
    Group getByCode(String code);

    /**
     * 根据用户ID获取组织列表，含岗位，组织，第一个为主组织
     *
     * @param userId
     * @return
     */
    List<Group> getByUserId(String userId);

}
