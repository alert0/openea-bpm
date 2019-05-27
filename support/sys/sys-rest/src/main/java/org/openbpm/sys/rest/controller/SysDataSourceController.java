package org.openbpm.sys.rest.controller;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.sys.core.manager.SysDataSourceManager;
import org.openbpm.sys.core.model.SysDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.core.id.IdUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.db.model.page.PageResult;
import org.openbpm.base.rest.GenericController;
import org.openbpm.base.rest.util.RequestUtil;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 * 描述：sysDataSource层的controller
 * </pre>
 */
@RestController
@RequestMapping("/sys/sysDataSource/")
public class SysDataSourceController extends GenericController {
    @Autowired
    SysDataSourceManager sysDataSourceManager;

    /**
     * <pre>
     * 测试连接
     * </pre>
     *
     * @param sysDataSource
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("checkConnection")
    @CatchErr(write2response = true, value = "连接失败")
    public ResultMsg<Boolean> checkConnection(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String key = RequestUtil.getString(request, "key");
    	String msg = "连接成功" ;
    	boolean connectable = true;
        try {
        	DataSource dataSource = sysDataSourceManager.getDataSourceByKey(key);
        	Connection connection = dataSource.getConnection();
            connection.close();
        } catch (Exception e) {
        	msg ="连接失败："+ e.getMessage();
        	connectable = false;
        	e.printStackTrace();
        }

        return getSuccessResult(connectable,msg);
    }

    /**
     * <pre>
     * sysDataSourceEdit.html的save后端
     * </pre>
     *
     * @param sysDataSource
     * @throws Exception
     */
    @RequestMapping("save")
    @CatchErr(write2response = true, value = "保存数据源失败")
    public ResultMsg<SysDataSource> save(@RequestBody SysDataSource sysDataSource) throws Exception {
        if (StringUtil.isEmpty(sysDataSource.getId())) {
            sysDataSource.setId(IdUtil.getSuid());
            sysDataSourceManager.create(sysDataSource);
        } else {
            sysDataSourceManager.update(sysDataSource);
        }

        return getSuccessResult(sysDataSource, "保存数据源成功");
    }

    /**
     * <pre>
     * 获取sysDataSource的后端
     * 目前支持根据id 获取sysDataSource
     * </pre>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getObject")
    @CatchErr(write2response = true, value = "获取sysDataSource异常")
    public ResultMsg<SysDataSource> getObject(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = RequestUtil.getString(request, "id");
        SysDataSource sysDataSource = null;
        if (StringUtil.isNotEmpty(id)) {
            sysDataSource = sysDataSourceManager.get(id);
        }
        return getSuccessResult(sysDataSource);
    }

    /**
     * <pre>
     * list页的后端
     * </pre>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("listJson")
    @ResponseBody
    public PageResult listJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
        QueryFilter queryFilter = getQueryFilter(request);
        List<SysDataSource> list = sysDataSourceManager.query(queryFilter);
        return new PageResult(list);
    }

    /**
     * <pre>
     * 批量删除
     * </pre>
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("remove")
    @CatchErr(write2response = true, value = "删除数据源失败")
    public ResultMsg<String> remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] aryIds = RequestUtil.getStringAryByStr(request, "id");
        sysDataSourceManager.removeByIds(aryIds);
        return getSuccessResult("删除数据源成功");
    }
}
