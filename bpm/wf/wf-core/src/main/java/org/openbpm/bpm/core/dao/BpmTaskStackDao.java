package org.openbpm.bpm.core.dao;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.bpm.core.model.BpmTaskStack;
import java.util.List;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BpmTaskStackDao extends BaseDao<String, BpmTaskStack> {
    List<BpmTaskStack> getByInstanceId(String str);

    BpmTaskStack getByTaskId(String str);

    void removeByInstanceId(String str);
}
