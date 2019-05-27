package org.openbpm.demo.core.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.demo.core.dao.DemoSubDao;
import org.openbpm.demo.core.model.DemoSub;
import org.openbpm.demo.core.manager.DemoSubManager;
/**
 * Demo子表 Manager处理实现类
 */
@Service("demoSubManager")
public class DemoSubManagerImpl extends BaseManager<String, DemoSub> implements DemoSubManager{
	@Resource
	DemoSubDao demoSubDao;

	public List<DemoSub> getByFk(String fk) {
		return demoSubDao.getByFk(fk);
	}

}
