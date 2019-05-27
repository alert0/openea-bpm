package org.openbpm.demo.rest.controller;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.openbpm.base.rest.BaseController;

import org.openbpm.demo.core.manager.DemoManager;
import org.openbpm.demo.core.model.Demo;


/**
 * 案例 控制器类<br/>
 * </pre>
 */
@RestController
@RequestMapping("/demo/demo")
public class DemoController extends BaseController<Demo>{
	@Resource
	DemoManager demoManager;
	
	
	@Override
	protected String getModelDesc() {
		return "案例";
	}
	   
}
