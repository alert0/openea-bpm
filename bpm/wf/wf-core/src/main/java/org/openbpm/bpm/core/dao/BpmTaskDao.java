package org.openbpm.bpm.core.dao;

import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.dao.BaseDao;
import org.openbpm.bpm.core.model.BpmTask;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BpmTaskDao extends BaseDao<String, BpmTask> {
    List<BpmTask> getByInstIdNodeId(@Param("instId") String str, @Param("nodeId") String str2);

    List<BpmTask> getTodoList(QueryFilter queryFilter);

    void removeByInstId(@Param("instId") String str);
}
