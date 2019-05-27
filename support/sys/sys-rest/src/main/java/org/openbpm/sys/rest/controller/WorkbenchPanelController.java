package org.openbpm.sys.rest.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openbpm.sys.core.manager.WorkbenchLayoutManager;
import org.openbpm.sys.core.manager.WorkbenchPanelManager;
import org.openbpm.sys.core.model.WorkbenchLayout;
import org.openbpm.sys.core.model.WorkbenchPanel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.db.model.query.DefaultQueryFilter;
import org.openbpm.base.rest.BaseController;
import org.openbpm.base.rest.util.RequestUtil;
import org.openbpm.sys.util.ContextUtil;


@RestController
@RequestMapping("/sys/workbenchPanel") 
public class WorkbenchPanelController extends BaseController<WorkbenchPanel>{
	@Resource
    WorkbenchPanelManager workbenchPanelManager;
	@Resource
    WorkbenchLayoutManager workbenchLayoutMananger;
	
	@RequestMapping("getPanelData")
	@CatchErr
	public  ResultMsg getPanelData(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String panelId = RequestUtil.getString(request, "panelId_");
		String dataSource = RequestUtil.getString(request,"dataSource_");
		String dataType = RequestUtil.getString(request,"dataType_");
		
		JSON json = null ;
		// 调用自定义查询的数据
		//由于自定义查询数据源存在切换，故逻辑写此处即可
		if(WorkbenchPanel.DATA_TYPE_CUST_QUERY.equals(dataType)){
			json = getCustQueryData(request,dataSource);
		}else{
			DefaultQueryFilter filter = (DefaultQueryFilter) getQueryFilter(request);
			//将request请求的参数都put进去。用的时候可以用
			filter.getParams().putAll(RequestUtil.getParameterValueMap(request,false));
			json = workbenchPanelManager.getDataByInterFace(filter,dataSource);
		}
		
		return new ResultMsg(json);
	}


	/**
	 * 调用自定义查询
	 * @param request
	 * @param dataSource
	 * @return
	 */
	private JSON getCustQueryData(HttpServletRequest request, String dataSource) {
		/*String queryData = RequestUtil.getString(request, "querydata");
		int page = RequestUtil.getInt(request, "page", 1);
		String needPage = RequestUtil.getString(request, "needPage","");

		CustomQuery customQuery = customQueryManager.getByAlias(dataSource);
		if (customQuery==null) { return null; }
		int pageSize = RequestUtil.getInt(request, "pageSize", customQuery.getPageSize());

		String dbType= SysDataSourceUtil.getDbType(customQuery.getDsalias());
		
		if(StringUtil.isNotEmpty(needPage)){
			customQuery.setNeedPage("false".equals(needPage)?0:1);
		}
		
		// 切换这次进程的数据源
		DbContextHolder.setDataSource(customQuery.getDsalias(),dbType);
		Page pageList = (Page)customQueryManager.getData(customQuery, queryData,dbType, page, pageSize);
		
		return (JSON) JSONObject.toJSON(new PageJson(pageList));*/
		return null;
	}
	
	
	@RequestMapping("removeMyPanel")
	@CatchErr
	public  ResultMsg removeMyPanel(HttpServletRequest request,HttpServletResponse response,String layoutId) throws Exception{
		workbenchLayoutMananger.remove(layoutId);
		return getSuccessResult("移除成功！");
	}
	
	@RequestMapping("saveMyPanel")
	@CatchErr
	public  ResultMsg saveMyPanel(HttpServletRequest request,HttpServletResponse response,String layoutId) throws Exception{
		String layoutKey = RequestUtil.getString(request, "layoutKey",ContextUtil.getCurrentUserId());
		String layoutListStr = RequestUtil.getString(request, "layoutList");
		
		List<WorkbenchLayout> layOutList = JSON.parseArray(layoutListStr, WorkbenchLayout.class);
		workbenchLayoutMananger.savePanelLayout(layOutList,layoutKey);
		return getSuccessResult("布局更新成功");
	}
	
	
	@RequestMapping("getMyWorkbench")
	@CatchErr
	public  ResultMsg getMyWorkbench(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String layoutKey = RequestUtil.getString(request, "layoutKey");
		
		List<WorkbenchPanel> workbenchPanelList = null;
		
		if(StringUtil.isNotEmpty(layoutKey)){
			workbenchPanelList = workbenchPanelManager.getBylayoutKey(layoutKey);
		}else{
			workbenchPanelList = workbenchPanelManager.getByUserId(ContextUtil.getCurrentUserId());
		}
		
		return new ResultMsg(workbenchPanelList);
	}


	@Override
	protected String getModelDesc() {
		return "工作台面板";
	}

}
