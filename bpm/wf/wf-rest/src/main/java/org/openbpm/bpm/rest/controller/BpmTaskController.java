package org.openbpm.bpm.rest.controller;

import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.db.model.page.PageResult;
import org.openbpm.base.rest.GenericController;
import org.openbpm.bpm.api.engine.action.cmd.FlowRequestParam;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.engine.data.BpmFlowDataAccessor;
import org.openbpm.bpm.api.engine.data.result.FlowData;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.bpm.core.manager.BpmTaskManager;
import org.openbpm.bpm.core.model.BpmTask;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.openbpm.bpm.plugin.usercalc.util.UserCalcPreview;
import org.openbpm.form.api.model.FormType;
import org.openbpm.sys.api.model.SysIdentity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping({"/bpm/task"})
@RestController
@Api(description = "流程任务")
public class BpmTaskController extends GenericController {
    @Resource
    BpmFlowDataAccessor bpmFlowDataAccessor;
    @Autowired
    BpmProcessDefService bpmProcessDefService;
    @Resource
    BpmTaskManager bpmTaskManager;

    @ApiOperation(notes = "获取流程任务的列表数据", value = "流程任务列表")
    @RequestMapping(method = {RequestMethod.POST}, value = {"listJson"})
    @ApiImplicitParams({@ApiImplicitParam(dataType = "String", name = "offset", paramType = "form", value = "offset"), @ApiImplicitParam(dataType = "String", name = "limit", paramType = "form", value = "分页大小"), @ApiImplicitParam(dataType = "String", name = "sort", paramType = "form", value = "排序字段"), @ApiImplicitParam(dataType = "String", defaultValue = "ASC", name = "order", paramType = "form", value = "order"), @ApiImplicitParam(dataType = "String", name = "filter$VEQ", paramType = "form", value = "其他过滤参数，“filter”为数据库过滤字段名，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")})
    public PageResult listJson(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
        return new PageResult(this.bpmTaskManager.query(getQueryFilter(request)));
    }

    @ApiOperation(notes = "获取流程任务", value = "流程任务")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, value = {"getBpmTask"})
    @CatchErr
    public ResultMsg<BpmTask> getBpmTask(@ApiParam(required = true, value = "任务ID") @RequestParam String id) throws Exception {
        BpmTask bpmTask = null;
        if (StringUtil.isNotEmpty(id)) {
            bpmTask = (BpmTask) this.bpmTaskManager.get(id);
        }
        return getSuccessResult(bpmTask);
    }

    @RequestMapping({"remove"})
    @CatchErr("删除流程任务失败")
    public ResultMsg<String> remove(@RequestParam String id) throws Exception {
        this.bpmTaskManager.removeByIds(StringUtil.getStringAryByStr(id));
        return getSuccessResult("删除流程任务成功");
    }

    @ApiOperation(notes = "获取任务业务数据，表单按钮等信息", value = "获取流程任务相关数据")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, value = {"getTaskData"})
    @CatchErr
    public ResultMsg<FlowData> getTaskData(@ApiParam(required = true, value = "任务ID") @RequestParam String taskId, @ApiParam(defaultValue = "pc", value = "表单类型") @RequestParam(required = false) String formType) throws Exception {
        if (StringUtil.isEmpty(formType)) {
            formType = FormType.PC.value();
        }
        return getSuccessResult(this.bpmFlowDataAccessor.getFlowTaskData(taskId, FormType.fromValue(formType)));
    }

    @ApiOperation(notes = "执行同意，驳回，反对，锁定，解锁，人工终止，会签任务等相关操作", value = "执行任务相关动作")
    @RequestMapping(method = {RequestMethod.POST}, value = {"doAction"})
    @CatchErr
    public ResultMsg<String> doAction(@RequestBody FlowRequestParam flowParam) throws Exception {
        return getSuccessResult(String.format("执行%s操作成功", new Object[]{new DefualtTaskActionCmd(flowParam).executeCmd()}));
    }

    @ApiOperation(notes = "管理员将任务取消指派，若任务原先无候选人，则无法取消指派。", value = "任务取消指派")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, value = {"unLock"})
    @CatchErr
    public ResultMsg<String> unLock(@RequestParam String taskId) throws Exception {
        this.bpmTaskManager.unLockTask(taskId);
        return getSuccessResult("取消指派成功");
    }

    @ApiOperation(notes = "管理员将任务指派给某一个用户处理", value = "任务指派")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, value = {"assignTask"})
    @CatchErr
    public ResultMsg<String> assignTask(@RequestParam String taskId, @RequestParam String userName, @RequestParam String userId) throws Exception {
        this.bpmTaskManager.assigneeTask(taskId, userId, userName);
        return getSuccessResult("指派成功");
    }

    @ApiOperation(notes = "根据配置，处理节点可自由选择下一个节点的执行人的逻辑", value = "处理节点 【自由选择候选人】功能")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, value = {"handleNodeFreeSelectUser"})
    @CatchErr
    public ResultMsg<Map<String, Object>> handleNodeFreeSelectUser(@RequestBody FlowRequestParam flowParam) throws Exception {
        Map<String, Object> result = new HashedMap();
        BpmTask task = (BpmTask) this.bpmTaskManager.get(flowParam.getTaskId());
        if (task == null) {
            throw new BusinessException(BpmStatusCode.TASK_NOT_FOUND);
        }
        BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(task.getDefId(), task.getNodeId());
        String freeSelectUser = nodeDef.getNodeProperties().getFreeSelectUser();
        result.put("type", freeSelectUser);
        boolean freeSelectNode = nodeDef.getNodeProperties().isFreeSelectNode();
        result.put("freeSelectNode", Boolean.valueOf(freeSelectNode));
        if (!"no".equals(freeSelectUser) || freeSelectNode) {
            handleNodeInfo(flowParam, task, nodeDef, result, freeSelectUser);
        }
        return getSuccessResult(result);
    }

    private void handleNodeInfo(FlowRequestParam flowParam, BpmTask task, BpmNodeDef nodeDef, Map<String, Object> result, String freeSelectUser) {
        DefualtTaskActionCmd taskModel = new DefualtTaskActionCmd(flowParam);
        taskModel.setBpmTask(task);
        BpmContext.setActionModel(taskModel);
        Map<String, String> nodeNameMap = new HashedMap();
        Map<String, List<SysIdentity>> nodeIdentitysMap = new HashedMap();
        for (BpmNodeDef node : nodeDef.getOutcomeTaskNodes()) {
            nodeNameMap.put(node.getNodeId(), node.getName());
            nodeIdentitysMap.put(node.getNodeId(), UserCalcPreview.calcNodeUsers(node, taskModel));
        }
        result.put("nodeIdentitysMap", nodeIdentitysMap);
        result.put("nodeNameMap", nodeNameMap);
        BpmContext.cleanTread();
    }
}
