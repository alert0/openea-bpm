package org.openbpm.demo.core.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.core.id.IdUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.bpm.api.engine.action.cmd.ActionCmd;
import org.openbpm.demo.core.dao.DemoDao;
import org.openbpm.demo.core.manager.DemoManager;
import org.openbpm.demo.core.manager.DemoSubManager;
import org.openbpm.demo.core.model.Demo;
import org.openbpm.demo.core.model.DemoSub;
/**
 * 案例 Manager处理实现类
 */
@Service("demoManager")
public class DemoManagerImpl extends BaseManager<String, Demo> implements DemoManager{
	@Resource
	DemoDao demoDao;
	@Resource
	DemoSubManager demoSubMananger;

	@Override
	public Demo get(String entityId) {
		Demo demo = super.get(entityId);
		if(demo == null) return null;
		
		List<DemoSub> demoSublist = demoSubMananger.getByFk(entityId);
		demo.setDemoSubList(demoSublist);
		
		return demo;
	}
	
	
	

	/**
	 * URL 表单案例
	 */
	@Override
	public void saveDemoJson(ActionCmd actionCmd) {
		String bizKey = actionCmd.getBusinessKey();
		JSONObject object = actionCmd.getBusData();
		Demo demo = JSON.toJavaObject(object, Demo.class);
		
		if(StringUtil.isEmpty(bizKey)) {
			String id = IdUtil.getSuid();
			actionCmd.setBusinessKey(id);
			demo.setId(id);
			demoDao.create(demo);
			
			Map<String,Object> hashMap = new HashMap<>();
			hashMap.put("startVariable", demo.getMz());
			//启动时候设置一些流程变量，请看 act_ru_variable 表、在整个流程声明周期您都可以使用该流程变量，可以用于分支判断等等
			actionCmd.setActionVariables(hashMap);
		}else {
			demoDao.update(demo);
			Map<String,Object> hashMap = new HashMap<>();
			hashMap.put("doTaskVariable", demo.getMz() + "-" + actionCmd.getActionName());
			actionCmd.setActionVariables(hashMap);
		}
	}
	
}
