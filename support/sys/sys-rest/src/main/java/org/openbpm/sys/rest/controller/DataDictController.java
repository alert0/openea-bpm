package org.openbpm.sys.rest.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.openbpm.sys.core.manager.DataDictManager;
import org.openbpm.sys.core.model.DataDict;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.openbpm.base.rest.BaseController;
import com.alibaba.fastjson.JSONArray;
import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.api.query.QueryOP;
import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.base.core.util.StringUtil;


/**
 * 数据字典 控制器类<br/>
 */
@RestController
@RequestMapping("/sys/dataDict")
public class DataDictController extends BaseController<DataDict>{
	@Resource
    DataDictManager dataDictManager;
	
	
	@Override
	protected String getModelDesc(){
		return "数据字典";
	}
	
	@RequestMapping("getDictData")
	public ResultMsg<List<DataDict>> getByDictKey(@RequestParam String dictKey,@RequestParam(defaultValue="false") Boolean hasRoot) throws Exception{
		if(StringUtil.isEmpty(dictKey)) return null;
		
		List<DataDict> dict = dataDictManager.getDictNodeList(dictKey,hasRoot);
		return getSuccessResult(dict);
	}
	
	@RequestMapping("getDictList")
	public ResultMsg<List<DataDict>> getDictList(HttpServletRequest request) throws Exception{
		QueryFilter filter = getQueryFilter(request);
		filter.addFilter("dict_type_", DataDict.TYPE_DICT, QueryOP.EQUAL);
		filter.setPage(null);
		
		List<DataDict> dict = dataDictManager.query(filter);
		return getSuccessResult(dict);
	}
	
	/**
	 * 获取所有数据字典，以tree的形式
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getDictTree")
	@CatchErr("获取数据字典失败")
	public ResultMsg<JSONArray> getDictTree() throws Exception{
		JSONArray dict = dataDictManager.getDictTree();
		return getSuccessResult(dict);
	}
	
}
