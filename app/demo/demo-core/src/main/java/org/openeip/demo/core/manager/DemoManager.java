package org.openbpm.demo.core.manager;

import org.openbpm.base.manager.Manager;
import org.openbpm.bpm.api.engine.action.cmd.ActionCmd;
import org.openbpm.demo.core.model.Demo;

/**
 * 案例 Manager处理接口
 */
public interface DemoManager extends Manager<String, Demo>{
	
	public void saveDemoJson(ActionCmd  actionCmd);
	
}
