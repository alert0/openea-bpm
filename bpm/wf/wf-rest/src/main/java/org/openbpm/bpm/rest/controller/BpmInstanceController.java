package org.openbpm.bpm.rest.controller;

import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.api.query.QueryOP;
import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.db.model.page.PageResult;
import org.openbpm.base.rest.GenericController;
import org.openbpm.base.rest.util.RequestUtil;
import org.openbpm.bpm.act.service.ActInstanceService;
import org.openbpm.bpm.api.engine.action.cmd.FlowRequestParam;
import org.openbpm.bpm.api.engine.data.BpmFlowDataAccessor;
import org.openbpm.bpm.api.engine.data.result.FlowData;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.model.inst.IBpmInstance;
import org.openbpm.bpm.api.service.BpmImageService;
import org.openbpm.bpm.core.manager.BpmDefinitionManager;
import org.openbpm.bpm.core.manager.BpmInstanceManager;
import org.openbpm.bpm.core.manager.BpmTaskOpinionManager;
import org.openbpm.bpm.core.model.BpmDefinition;
import org.openbpm.bpm.core.model.BpmInstance;
import org.openbpm.bpm.core.model.BpmTaskOpinion;
import org.openbpm.bpm.engine.action.cmd.DefaultInstanceActionCmd;
import org.openbpm.form.api.model.FormType;
import org.openbpm.sys.util.ContextUtil;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping({"/bpm/instance"})
@RestController
@Api(description = "流程实例")
public class BpmInstanceController extends GenericController {
    @Resource
    ActInstanceService actInstanceService;
    @Resource
    BpmDefinitionManager bpmDefinitionMananger;
    @Resource
    BpmFlowDataAccessor bpmFlowDataAccessor;
    @Resource
    BpmImageService bpmImageService;
    @Resource
    BpmInstanceManager bpmInstanceManager;
    @Resource
    BpmTaskOpinionManager bpmTaskOpinionManager;

    @ApiOperation(notes = "获取流程实例列表", value = "流程实例列表")
    @RequestMapping(method = {RequestMethod.POST}, value = {"listJson"})
    @ApiImplicitParams({@ApiImplicitParam(dataType = "String", name = "offset", paramType = "form", value = "offset"), @ApiImplicitParam(dataType = "String", name = "limit", paramType = "form", value = "分页大小"), @ApiImplicitParam(dataType = "String", name = "sort", paramType = "form", value = "排序字段"), @ApiImplicitParam(dataType = "String", defaultValue = "ASC", name = "order", paramType = "form", value = "order"), @ApiImplicitParam(dataType = "String", name = "filter$VEQ", paramType = "form", value = "其他过滤参数，“filter”为数据库过滤字段名，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")})
    public PageResult listJson(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
        return new PageResult((Page) this.bpmInstanceManager.query(getQueryFilter(request)));
    }

    @ApiOperation(notes = "获取流程实例列表-当前组织", value = "流程实例列表-当前组织")
    @RequestMapping(method = {RequestMethod.POST}, value = {"listJson_currentOrg"})
    @ApiImplicitParams({@ApiImplicitParam(dataType = "String", name = "offset", paramType = "form", value = "offset"), @ApiImplicitParam(dataType = "String", name = "limit", paramType = "form", value = "分页大小"), @ApiImplicitParam(dataType = "String", name = "sort", paramType = "form", value = "排序字段"), @ApiImplicitParam(dataType = "String", defaultValue = "ASC", name = "order", paramType = "form", value = "order"), @ApiImplicitParam(dataType = "String", name = "filter$VEQ", paramType = "form", value = "其他过滤参数，“filter”为数据库过滤字段名，“V”一位代表字段类型，“EQ/IN/LK/..”代表查询类型“等于/in/小于/..”")})
    public PageResult listJson_currentOrg(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
        QueryFilter queryFilter = getQueryFilter(request);
        if (StringUtil.isEmpty(ContextUtil.getCurrentGroupId())) {
            return new PageResult();
        }
        queryFilter.addFilter("create_org_id_", ContextUtil.getCurrentGroupId(), QueryOP.EQUAL);
        return new PageResult((Page) this.bpmInstanceManager.query(queryFilter));
    }

    @ApiOperation(notes = "获取流程实例", value = "流程实例")
    @RequestMapping(method = {RequestMethod.POST}, value = {"getById"})
    @CatchErr
    public ResultMsg<IBpmInstance> getBpmInstance(@ApiParam("ID") @RequestParam String id) throws Exception {
        IBpmInstance bpmInstance = null;
        if (StringUtil.isNotEmpty(id)) {
            bpmInstance = (IBpmInstance) this.bpmInstanceManager.get(id);
        }
        return getSuccessResult(bpmInstance);
    }

    @ApiOperation(notes = "获取流程实例相关数据", value = "流程实例数据")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, value = {"getInstanceData"})
    @CatchErr
    @ApiImplicitParams({@ApiImplicitParam(dataType = "String", name = "instanceId", paramType = "form", value = "流程实例ID"), @ApiImplicitParam(dataType = "String", defaultValue = "false", name = "readonly", paramType = "form", value = "是否只读实例"), @ApiImplicitParam(dataType = "String", name = "defId", paramType = "form", value = "流程定义ID，启动时使用"), @ApiImplicitParam(dataType = "String", name = "flowKey", paramType = "form", value = "流程定义Key，启动时使用,与DefId二选一"), @ApiImplicitParam(dataType = "String", defaultValue = "pc", name = "formType", paramType = "form", value = "表单类型")})
    public ResultMsg<FlowData> getInstanceData(HttpServletRequest request) throws Exception {
        String instanceId = request.getParameter("instanceId");
        Boolean readonly = Boolean.valueOf(RequestUtil.getBoolean(request, "readonly", false));
        String defId = request.getParameter("defId");
        String flowKey = RequestUtil.getString(request, "flowKey");
        if (StringUtil.isEmpty(defId) && StringUtil.isNotEmpty(flowKey)) {
            BpmDefinition def = this.bpmDefinitionMananger.getByKey(flowKey);
            if (def == null) {
                throw new BusinessException("流程定义查找失败！ flowKey： " + flowKey, BpmStatusCode.DEF_LOST);
            }
            defId = def.getId();
        }
        return getSuccessResult(this.bpmFlowDataAccessor.getStartFlowData(defId, instanceId, FormType.fromValue(RequestUtil.getString(request, "formType", FormType.PC.value())), readonly));
    }

    @ApiOperation(notes = "流程启动，流程保存草稿，草稿启动等流程实例相关的动作", value = "执行流程实例相关动作")
    @RequestMapping(method = {RequestMethod.POST}, value = {"doAction"})
    @CatchErr
    public ResultMsg<String> doAction(@RequestBody FlowRequestParam flowParam) throws Exception {
        DefaultInstanceActionCmd instanceCmd = new DefaultInstanceActionCmd(flowParam);
        return getSuccessResult(instanceCmd.getInstanceId(), instanceCmd.executeCmd() + "成功");
    }

    @ApiOperation("获取流程意见")
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = {"getInstanceOpinion"})
    @CatchErr
    public ResultMsg<List<BpmTaskOpinion>> getInstanceOpinion(@ApiParam("流程实例ID") @RequestParam String instId) throws Exception {
        return getSuccessResult(this.bpmTaskOpinionManager.getByInstId(instId), "获取流程意见成功");
    }

    @ApiOperation("获取流程图流文件")
    @RequestMapping(method = {RequestMethod.GET}, value = {"flowImage"})
    public void flowImage(@ApiParam("流程实例ID") @RequestParam(required = false) String instId, @ApiParam("流程定义ID，流程未启动时使用") @RequestParam(required = false) String defId, HttpServletResponse response) throws Exception {
        String actDefId;
        String actInstId = null;
        if (StringUtil.isNotEmpty(instId)) {
            BpmInstance inst = (BpmInstance) this.bpmInstanceManager.get(instId);
            actInstId = inst.getActInstId();
            actDefId = inst.getActDefId();
        } else {
            actDefId = ((BpmDefinition) this.bpmDefinitionMananger.get(defId)).getActDefId();
        }
        response.setContentType("image/png");
        IOUtils.copy(this.bpmImageService.draw(actDefId, actInstId), response.getOutputStream());
    }

    @ApiOperation("流程实例禁用/启用")
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = {"toForbidden"})
    @CatchErr("操作失败")
    public ResultMsg<String> toForbidden(@ApiParam("流程实例ID") @RequestParam String id, @ApiParam("禁用/启用") @RequestParam Boolean forbidden) throws Exception {
        BpmInstance inst = (BpmInstance) this.bpmInstanceManager.get(id);
        String msg = "";
        if (forbidden.booleanValue() && inst.getIsForbidden().shortValue() == 0) {
            inst.setIsForbidden(IBpmInstance.INSTANCE_FORBIDDEN);
            msg = "禁用成功";
        } else if (!forbidden.booleanValue()) {
            inst.setIsForbidden(IBpmInstance.INSTANCE_NO_FORBIDDEN);
            msg = "取消禁用成功";
        }
        this.bpmInstanceManager.update(inst);
        return getSuccessResult(msg);
    }

    @RequestMapping({"startTest"})
    @CatchErr
    public ResultMsg<String> startTest(@RequestParam String flowData) throws Exception {
        this.actInstanceService.startProcessInstance("process:2:10000000890004", "test", null);
        return getSuccessResult("成功");
    }

    @RequestMapping({"delete"})
    @CatchErr
    public ResultMsg<String> delete(@RequestParam String id) throws Exception {
        this.bpmInstanceManager.delete(id);
        return getSuccessResult("删除实例成功");
    }
}
