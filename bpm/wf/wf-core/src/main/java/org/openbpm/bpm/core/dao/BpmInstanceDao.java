package org.openbpm.bpm.core.dao;

import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.dao.BaseDao;
import org.openbpm.bpm.core.model.BpmInstance;
import org.openbpm.bpm.core.model.BpmTaskApprove;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BpmInstanceDao extends BaseDao<String, BpmInstance> {
    List<BpmInstance> getApplyList(QueryFilter queryFilter);

    List<BpmTaskApprove> getApproveHistoryList(QueryFilter queryFilter);

    BpmInstance getByActInstId(@Param("actInstId") String str);

    List<BpmInstance> getByPId(@Param("parentInstId") String str);
}
