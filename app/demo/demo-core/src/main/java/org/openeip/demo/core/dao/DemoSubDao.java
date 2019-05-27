package org.openbpm.demo.core.dao;
import java.util.List;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.demo.core.model.DemoSub;

/**
 * Demo子表 DAO接口
 */
public interface DemoSubDao extends BaseDao<String, DemoSub> {

	List<DemoSub> getByFk(String fk);

}
