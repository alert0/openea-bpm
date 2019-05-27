package org.openbpm.bpm.core.dao;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.bpm.core.model.BpmBusLink;
import java.util.List;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BpmBusLinkDao extends BaseDao<String, BpmBusLink> {
    List<BpmBusLink> getByInstanceId(String str);
}
