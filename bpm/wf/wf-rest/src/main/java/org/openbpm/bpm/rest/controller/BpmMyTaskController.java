package org.openbpm.bpm.rest.controller;

import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.api.query.QueryOP;
import org.openbpm.base.db.model.page.PageResult;
import org.openbpm.base.rest.GenericController;
import org.openbpm.bpm.api.constant.InstanceStatus;
import org.openbpm.bpm.core.manager.BpmDefinitionManager;
import org.openbpm.bpm.core.manager.BpmInstanceManager;
import org.openbpm.bpm.core.manager.BpmTaskManager;
import org.openbpm.sys.util.ContextUtil;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping({"/bpm/my"})
@RestController
@Api(description = "个人办公")
public class BpmMyTaskController extends GenericController {
    @Resource
    BpmDefinitionManager bpmDefinitionManager;
    @Resource
    BpmInstanceManager bpmInstanceManager;
    @Resource
    BpmTaskManager bpmTaskManager;

    @ApiOperation(notes = "根据当前用户获取个人所有待办", value = "我的待办")
    @RequestMapping(method = {RequestMethod.POST}, value = {"todoTaskList"})
    @ApiImplicitParams({@ApiImplicitParam(dataType = "String", name = "offset", paramType = "form", value = "offset"), @ApiImplicitParam(dataType = "String", name = "limit", paramType = "form", value = "分页大小，默认20条"), @ApiImplicitParam(dataType = "String", name = "sort", paramType = "form", value = "排序字段"), @ApiImplicitParam(dataType = "String", defaultValue = "ASC", name = "order", paramType = "form", value = "排序，默认升序"), @ApiImplicitParam(dataType = "String", name = "filter$VEQ", paramType = "form", value = "过滤参数。“filter”为数据库过滤字段名,“V”一位代表字段类型,“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")})
    public PageResult todoTaskList(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
        QueryFilter queryFilter = getQueryFilter(request);
        return new PageResult((Page) this.bpmTaskManager.getTodoList(ContextUtil.getCurrentUserId(), queryFilter));
    }

    @ApiOperation(notes = "获取历史发起的流程申请", value = "我的申请")
    @RequestMapping(method = {RequestMethod.POST}, value = {"applyTaskList"})
    @ApiImplicitParams({@ApiImplicitParam(dataType = "String", name = "offset", paramType = "form", value = "offset"), @ApiImplicitParam(dataType = "String", name = "limit", paramType = "form", value = "分页大小"), @ApiImplicitParam(dataType = "String", name = "sort", paramType = "form", value = "排序字段"), @ApiImplicitParam(dataType = "String", defaultValue = "ASC", name = "order", paramType = "form", value = "order"), @ApiImplicitParam(dataType = "String", name = "filter$VEQ", paramType = "form", value = "其他过滤参数，“filter”为数据库过滤字段名，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")})
    public PageResult applyTaskList(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
        QueryFilter queryFilter = getQueryFilter(request);
        return new PageResult((Page) this.bpmInstanceManager.getApplyList(ContextUtil.getCurrentUserId(), queryFilter));
    }

    @ApiOperation(notes = "获取拥有权限的流程列表", value = "发起申请")
    @RequestMapping(method = {RequestMethod.POST}, value = {"definitionList"})
    @ApiImplicitParams({@ApiImplicitParam(dataType = "String", name = "offset", paramType = "form", value = "offset"), @ApiImplicitParam(dataType = "String", name = "limit", paramType = "form", value = "分页大小"), @ApiImplicitParam(dataType = "String", name = "sort", paramType = "form", value = "排序字段"), @ApiImplicitParam(dataType = "String", defaultValue = "ASC", name = "order", paramType = "form", value = "order"), @ApiImplicitParam(dataType = "String", name = "filter$VEQ", paramType = "form", value = "其他过滤参数，“filter”为数据库过滤字段名，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")})
    public PageResult definitionList(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
        QueryFilter queryFilter = getQueryFilter(request);
        return new PageResult(this.bpmDefinitionManager.getMyDefinitionList(ContextUtil.getCurrentUserId(), queryFilter));
    }

    @ApiOperation(notes = "获取审批过的流程任务", value = "我的审批")
    @RequestMapping(method = {RequestMethod.POST}, value = {"approveList"})
    @ApiImplicitParams({@ApiImplicitParam(dataType = "String", name = "offset", paramType = "form", value = "offset"), @ApiImplicitParam(dataType = "String", name = "limit", paramType = "form", value = "分页大小"), @ApiImplicitParam(dataType = "String", name = "sort", paramType = "form", value = "排序字段"), @ApiImplicitParam(dataType = "String", defaultValue = "ASC", name = "order", paramType = "form", value = "order"), @ApiImplicitParam(dataType = "String", name = "filter$VEQ", paramType = "form", value = "其他过滤参数，“filter”为数据库过滤字段名，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")})
    public PageResult approveList(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
        QueryFilter queryFilter = getQueryFilter(request);
        return new PageResult(this.bpmInstanceManager.getApproveHistoryList(ContextUtil.getCurrentUserId(), queryFilter));
    }

    @ApiOperation(notes = "获取我的草稿", value = "我的草稿")
    @RequestMapping(method = {RequestMethod.POST}, value = {"draftList"})
    @ApiImplicitParams({@ApiImplicitParam(dataType = "String", name = "offset", paramType = "form", value = "offset"), @ApiImplicitParam(dataType = "String", name = "limit", paramType = "form", value = "分页大小"), @ApiImplicitParam(dataType = "String", name = "sort", paramType = "form", value = "排序字段"), @ApiImplicitParam(dataType = "String", defaultValue = "ASC", name = "order", paramType = "form", value = "order"), @ApiImplicitParam(dataType = "String", name = "filter$VEQ", paramType = "form", value = "其他过滤参数，“filter”为数据库过滤字段名，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")})
    public PageResult draftList(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
        QueryFilter queryFilter = getQueryFilter(request);
        String userId = ContextUtil.getCurrentUserId();
        queryFilter.addFilter("inst.status_", InstanceStatus.STATUS_DRAFT.getKey(), QueryOP.EQUAL);
        return new PageResult(this.bpmInstanceManager.getApplyList(userId, queryFilter));
    }
}
