package org.openbpm.demo.core.manager;

import java.util.List;

import org.openbpm.base.manager.Manager;
import org.openbpm.demo.core.model.DemoSub;

/**
 * Demo子表 Manager处理接口
 */
public interface DemoSubManager extends Manager<String, DemoSub>{

	List<DemoSub> getByFk(String entityId);
}
