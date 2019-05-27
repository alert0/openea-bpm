package org.openbpm.form.rest.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.base.db.model.table.Column;
import org.openbpm.base.db.model.table.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.dao.CommonDao;
import org.openbpm.base.db.datasource.DbContextHolder;
import org.openbpm.base.db.model.page.PageResult;
import org.openbpm.base.rest.BaseController;
import org.openbpm.base.rest.util.RequestUtil;
import org.openbpm.form.manager.FormCustDialogManager;
import org.openbpm.form.model.FormCustDialog;
import org.openbpm.sys.api.model.ISysDataSource;
import org.openbpm.sys.api.service.ISysDataSourceService;

/**
 * <pre>
 * 描述：自定义对话框管理
 * </pre>
 */
@RestController
@RequestMapping("/form/formCustDialog/")
public class FormCustDialogController extends BaseController<FormCustDialog> {
    @Autowired
    FormCustDialogManager formCustDialogManager;
    @Autowired
    CommonDao<?> commonDao;
    
    @Autowired
    ISysDataSourceService sysDataSourceService;

    /**
     * <pre>
     * 获取formCustDialog的后端
     * 目前支持根据id 获取formCustDialog
     * </pre>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getObject")
    @CatchErr(write2response = true, value = "获取formCustDialog异常")
    public ResultMsg<FormCustDialog> getObject(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = RequestUtil.getString(request, "id");
        String key = RequestUtil.getString(request, "key");
        FormCustDialog formCustDialog = null;
        if (StringUtil.isNotEmpty(id)) {
            formCustDialog = formCustDialogManager.get(id);
        } else if (StringUtil.isNotEmpty(key)) {
            formCustDialog = formCustDialogManager.getByKey(key);
        }

        return getSuccessResult(formCustDialog);
    }

    /**
     * <pre>
     * edit页面的searchObjName
     * 其他页面使用可以参数传一个json:
     * {dsKey:"数据源key",objType:"枚举在 org.openbpm.form.api.model.FormCustDialogOBJTYPE ",objName:"要查询的名字"}
     * </pre>
     *
     * @param request
     * @param response
     * @param formCustDialog
     * @throws Exception
     */
    @RequestMapping("searchObjName")
    @CatchErr(write2response = true, value = "根据数据源获取objName信息失败")
    public ResultMsg<Map<String, String>> searchObjName(HttpServletRequest request, HttpServletResponse response, @RequestBody FormCustDialog formCustDialog) throws Exception {
       return getSuccessResult(formCustDialogManager.searchObjName(formCustDialog), "根据数据源获取objName信息成功");
    }

    /**
     * <pre>
     * 获取objName对象的信息
     * 其他页面使用可以参数传一个json:
     * {dsKey:"数据源key",objType:"枚举在org.openbpm.form.model.FormCustDialog$OBJTYPE",objName:"要查询的名字"}
     * </pre>
     *
     * @param request
     * @param response
     * @param formCustDialog
     * @throws Exception
     */
    @RequestMapping("getTable")
    @CatchErr(write2response = true, value = "根据数据源获取objName的字段信息失败")
    public ResultMsg<Table<Column>> getTable(HttpServletRequest request, HttpServletResponse response, @RequestBody FormCustDialog formCustDialog) throws Exception {
    	return getSuccessResult(formCustDialogManager.getTable(formCustDialog), "根据数据源获取objName的字段信息成功");
    }

    /**
     * <pre>
     * 获取对话框的列表数据后端
     * </pre>
     *
     * @param request
     * @param response
     * @param key
     * @return
     * @throws Exception
     */
    @RequestMapping("listData_{key}")
    @CatchErr(write2response = true, value = "获取对话框的列表数据失败")
    public PageResult listData(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "key") String key) throws Exception {
        QueryFilter queryFilter = getQueryFilter(request);
        // 页面来的参数
        FormCustDialog formCustDialog = formCustDialogManager.getByKey(key);
        ISysDataSource sysDataSource = sysDataSourceService.getByKey(formCustDialog.getDsKey());
        // 切换数据源
        if(sysDataSource != null) {
        	DbContextHolder.setDataSource(sysDataSource.getKey(), sysDataSource.getDbType());
        }
        List<?> list = formCustDialogManager.data(formCustDialog, queryFilter);
        return new PageResult(list);
    }

    /**
     * <pre>
     * 获取对话框的树数据后端
     * </pre>
     *
     * @param request
     * @param response
     * @param key
     * @return
     * @throws Exception
     */
    @RequestMapping("treeData_{key}")
    public List<?> treeData(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "key") String key) throws Exception {
        QueryFilter queryFilter = getQueryFilter(request);
        queryFilter.setPage(null);
        // 页面来的参数
        FormCustDialog formCustDialog = formCustDialogManager.getByKey(key);
        ISysDataSource sysDataSource = sysDataSourceService.getByKey(formCustDialog.getDsKey());
        // 切换数据源
        DbContextHolder.setDataSource(sysDataSource.getKey(), sysDataSource.getDbType());
        return formCustDialogManager.data(formCustDialog, queryFilter);
    }

    @Override
    protected String getModelDesc() {
        return "自定义对话框";
    }

}
