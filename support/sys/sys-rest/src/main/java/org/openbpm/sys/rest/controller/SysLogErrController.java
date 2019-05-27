package org.openbpm.sys.rest.controller;

import org.openbpm.sys.core.model.LogErr;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.openbpm.base.rest.BaseController;

@RestController
@RequestMapping("/sys/sysLogErr/")
public class SysLogErrController extends BaseController<LogErr> {

	@Override
	protected String getModelDesc() {
		return "系统异常日志";
	}

}
