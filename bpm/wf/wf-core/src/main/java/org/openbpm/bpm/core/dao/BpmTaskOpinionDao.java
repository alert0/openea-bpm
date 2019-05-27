package org.openbpm.bpm.core.dao;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.bpm.core.model.BpmTaskOpinion;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BpmTaskOpinionDao extends BaseDao<String, BpmTaskOpinion> {
    List<BpmTaskOpinion> getByInstAndNode(@Param("instId") String str, @Param("nodeId") String str2, @Param("token") String str3);

    BpmTaskOpinion getByTaskId(String str);

    void removeByInstId(@Param("instId") String str);
}
