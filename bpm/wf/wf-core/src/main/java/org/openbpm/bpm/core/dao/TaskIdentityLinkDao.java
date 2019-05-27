package org.openbpm.bpm.core.dao;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.bpm.core.model.TaskIdentityLink;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface TaskIdentityLinkDao extends BaseDao<String, TaskIdentityLink> {
    void bulkCreate(List<TaskIdentityLink> list);

    int checkUserOperatorPermission(@Param("rights") Set<String> set, @Param("taskId") String str, @Param("instanceId") String str2);

    List<TaskIdentityLink> getByTaskId(String str);

    int queryInstanceIdentityCount(String str);

    int queryTaskIdentityCount(String str);

    void removeByInstId(String str);

    void removeByTaskId(String str);
}
