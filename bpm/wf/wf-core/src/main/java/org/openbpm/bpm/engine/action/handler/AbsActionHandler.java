package org.openbpm.bpm.engine.action.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.api.exception.BusinessError;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.engine.action.cmd.ActionCmd;
import org.openbpm.bpm.api.engine.action.cmd.BaseActionCmd;
import org.openbpm.bpm.api.engine.action.cmd.InstanceActionCmd;
import org.openbpm.bpm.api.engine.action.cmd.TaskActionCmd;
import org.openbpm.bpm.api.engine.action.handler.ActionHandler;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.exception.WorkFlowException;
import org.openbpm.bpm.api.model.def.BpmDataModel;
import org.openbpm.bpm.api.model.def.NodeInit;
import org.openbpm.bpm.api.model.form.BpmForm;
import org.openbpm.bpm.api.model.inst.IBpmInstance;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.api.model.task.IBpmTask;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.bpm.core.manager.BpmInstanceManager;
import org.openbpm.bpm.core.manager.TaskIdentityLinkManager;
import org.openbpm.bpm.core.model.BpmInstance;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.openbpm.bpm.engine.constant.TaskSkipType;
import org.openbpm.bpm.engine.data.handle.BpmBusDataHandle;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import org.openbpm.bpm.engine.util.HandlerUtil;
import org.openbpm.bus.api.model.IBusinessData;
import org.openbpm.bus.api.service.IBusinessDataService;
import org.openbpm.org.api.model.IUser;
import org.openbpm.sys.api.groovy.IGroovyScriptEngine;
import org.openbpm.sys.util.ContextUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbsActionHandler<T extends BaseActionCmd> implements ActionHandler<T> {
    protected Logger LOG = LoggerFactory.getLogger(getClass());
    @Resource
    protected BpmInstanceManager bpmInstanceManager;
    @Resource
    protected BpmProcessDefService bpmProcessDefService;
    @Resource
    protected BpmBusDataHandle busDataHandle;
    @Resource
    protected IBusinessDataService iBusinessDataService;
    @Resource
    protected IGroovyScriptEngine iGroovyScriptEngine;
    @Resource
    protected TaskIdentityLinkManager taskIdentityLinkManager;

    protected abstract void doAction(T t);

    protected abstract void doActionAfter(T t);

    protected abstract void doActionBefore(T t);

    protected abstract void prepareActionDatas(T t);

    @Transactional
    public void execute(T model) {
        prepareActionDatas(model);
        checkFlowIsValid(model);
        BpmContext.setActionModel(model);
        handelBusData(model);
        doAction(model);
        doSkip(model);
        BpmContext.removeActionModel();
        actionAfter(model);
    }

    protected void handelBusData(T data) {
        executeHandler(data);
        this.LOG.debug("流程启动处理业务数据...");
        this.busDataHandle.saveDataModel(data);
        doActionBefore(data);
    }

    protected void actionAfter(T model) {
        doActionAfter(model);
        if (getActionType() != ActionType.DRAFT && model.isSource()) {
            BpmContext.cleanTread();
        }
    }

    private void doSkip(T t) {
        if (getActionType() == ActionType.AGREE || getActionType() == ActionType.OPPOSE || getActionType() == ActionType.START) {
            ActionCmd actionModel = BpmContext.getActionModel();
            if (actionModel != null && !(actionModel instanceof InstanceActionCmd)) {
                DefualtTaskActionCmd taskModel = (DefualtTaskActionCmd) BpmContext.getActionModel();
                if (!taskModel.getActionName().equals(ActionType.CREATE) && taskModel.isHasSkipThisTask() != TaskSkipType.NO_SKIP) {
                    DefualtTaskActionCmd complateModel = new DefualtTaskActionCmd();
                    complateModel.setBpmInstance(taskModel.getBpmInstance());
                    complateModel.setBpmDefinition(taskModel.getBpmDefinition());
                    complateModel.setBizDataMap(taskModel.getBizDataMap());
                    complateModel.setBpmIdentities(taskModel.getBpmIdentities());
                    complateModel.setBusinessKey(taskModel.getBusinessKey());
                    complateModel.setActionName(ActionType.AGREE.getKey());
                    complateModel.setBpmTask(taskModel.getBpmTask());
                    complateModel.setOpinion(taskModel.isHasSkipThisTask().getValue());
                    complateModel.executeSkipTask();
                }
            }
        }
    }

    public void skipTaskExecute(T model) {
        BpmContext.setActionModel(model);
        doActionBefore(model);
        doAction(model);
        doSkip(model);
        BpmContext.removeActionModel();
        actionAfter(model);
    }

    protected void executeHandler(T actionModel) {
        BpmInstance instance = (BpmInstance) actionModel.getBpmInstance();
        if (StringUtil.isEmpty(actionModel.getBusinessKey()) && StringUtil.isNotEmpty(instance.getBizKey())) {
            actionModel.setBusinessKey(instance.getBizKey());
        }
        String handler = getFormHandler(actionModel);
        if (StringUtil.isNotEmpty(handler)) {
            try {
                HandlerUtil.invokeHandler(actionModel, handler);
                this.LOG.debug("执行URL表单处理器：{}", handler);
            } catch (Exception ex) {
                throw new WorkFlowException(BpmStatusCode.HANDLER_ERROR, ex);
            }
        }
        if (StringUtil.isNotEmpty(actionModel.getBusinessKey()) && StringUtil.isEmpty(instance.getBizKey())) {
            instance.setBizKey(actionModel.getBusinessKey());
        }
    }

    protected void checkFlowIsValid(BaseActionCmd actionModel) {
        IBpmInstance instance = actionModel.getBpmInstance();
        if (instance.getIsForbidden().shortValue() == 1) {
            throw new WorkFlowException("流程实例已经被禁止，请联系管理员", BpmStatusCode.DEF_FORBIDDEN);
        }
        DefaultBpmProcessDef def = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(instance.getDefId());
        if ("forbidden".equals(def.getExtProperties().getStatus())) {
            throw new WorkFlowException("流程定义已经被禁用，请联系管理员", BpmStatusCode.DEF_FORBIDDEN);
        }
        IUser user = ContextUtil.getCurrentUser();
        if (!ContextUtil.isAdmin(user)) {
            String instanceId = actionModel.getInstanceId();
            if (actionModel instanceof DefualtTaskActionCmd) {
                IBpmTask task = ((DefualtTaskActionCmd) actionModel).getBpmTask();
                if (!user.getUserId().equals(task.getAssigneeId())) {
                    if (!this.taskIdentityLinkManager.checkUserOperatorPermission(user.getUserId(), null, task.getId()).booleanValue()) {
                        throw new BusinessException("没有该任务的操作权限", BpmStatusCode.NO_PERMISSION);
                    }
                    return;
                }
                return;
            }
            if (StringUtil.isNotEmpty(def.getProcessDefinitionId())) {
            }
        }
    }

    protected void parserBusinessData(T actionModel) {
        IBpmInstance instance = actionModel.getBpmInstance();
        JSONObject busData = actionModel.getBusData();
        if (busData != null && !busData.isEmpty()) {
            for (BpmDataModel dataModel : this.bpmProcessDefService.getBpmProcessDef(instance.getDefId()).getDataModelList()) {
                String modelCode = dataModel.getCode();
                if (busData.containsKey(modelCode)) {
                    actionModel.getBizDataMap().put(modelCode, this.iBusinessDataService.parseBusinessData(busData.getJSONObject(modelCode), modelCode));
                }
            }
        }
    }

    protected void handelFormInit(BaseActionCmd cmd, BpmNodeDef nodeDef) {
        List<NodeInit> nodeInitList = this.bpmProcessDefService.getBpmProcessDef(cmd.getBpmInstance().getDefId()).getNodeInitList(nodeDef.getNodeId());
        Map<String, IBusinessData> bos = cmd.getBizDataMap();
        if (!MapUtil.isEmpty(bos) && !CollectionUtil.isEmpty(nodeInitList)) {
            Map<String, Object> param = new HashMap<>();
            param.putAll(bos);
            param.put("bpmInstance", cmd.getBpmInstance());
            if (cmd instanceof DefualtTaskActionCmd) {
                param.put("bpmTask", ((DefualtTaskActionCmd) cmd).getBpmTask());
            }
            for (NodeInit init : nodeInitList) {
                if (StringUtil.isNotEmpty(init.getWhenSave())) {
                    try {
                        this.iGroovyScriptEngine.execute(init.getWhenSave(), param);
                        this.LOG.debug("执行节点数据初始化脚本{}", init.getBeforeShow());
                    } catch (Exception e) {
                        throw new BusinessError(e.getMessage(), BpmStatusCode.FLOW_DATA_EXECUTE_SHOWSCRIPT_ERROR, e);
                    }
                }
            }
        }
    }

    private String getFormHandler(T actionModel) {
        BpmForm form;
        String defId = actionModel.getDefId();
        if (actionModel instanceof TaskActionCmd) {
            form = this.bpmProcessDefService.getBpmNodeDef(defId, ((TaskActionCmd) actionModel).getNodeId()).getForm();
        } else {
            form = this.bpmProcessDefService.getStartEvent(defId).getForm();
        }
        if (form == null || form.isFormEmpty()) {
            form = this.bpmProcessDefService.getBpmProcessDef(defId).getGlobalForm();
        }
        if (form != null) {
            return form.getFormHandler();
        }
        return null;
    }

    public Boolean isDefault() {
        return Boolean.valueOf(true);
    }

    public String getDefaultBeforeScript() {
        return "";
    }
}
