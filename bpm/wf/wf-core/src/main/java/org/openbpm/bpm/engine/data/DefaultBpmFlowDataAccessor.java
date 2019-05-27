package org.openbpm.bpm.engine.data;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.api.exception.BusinessError;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.api.exception.BusinessMessage;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.engine.action.button.ButtonFactory;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.engine.data.BpmFlowDataAccessor;
import org.openbpm.bpm.api.engine.data.result.BpmFlowData;
import org.openbpm.bpm.api.engine.data.result.BpmFlowInstanceData;
import org.openbpm.bpm.api.engine.data.result.BpmFlowTaskData;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.model.def.NodeInit;
import org.openbpm.bpm.api.model.form.BpmForm;
import org.openbpm.bpm.api.model.inst.IBpmInstance;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.api.model.nodedef.Button;
import org.openbpm.bpm.api.model.task.IBpmTask;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.bpm.api.service.BpmRightsFormService;
import org.openbpm.bpm.core.manager.BpmBusLinkManager;
import org.openbpm.bpm.core.manager.BpmDefinitionManager;
import org.openbpm.bpm.core.manager.BpmInstanceManager;
import org.openbpm.bpm.core.manager.BpmTaskManager;
import org.openbpm.bpm.core.model.BpmInstance;
import org.openbpm.bpm.engine.data.handle.BpmBusDataHandle;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import org.openbpm.bus.api.model.IBusinessData;
import org.openbpm.bus.api.model.IBusinessPermission;
import org.openbpm.bus.api.service.IBusinessDataService;
import org.openbpm.form.api.model.FormCategory;
import org.openbpm.form.api.model.FormType;
import org.openbpm.form.api.model.IFormDef;
import org.openbpm.form.api.service.FormService;
import org.openbpm.sys.api.groovy.IGroovyScriptEngine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultBpmFlowDataAccessor implements BpmFlowDataAccessor {
    protected Logger LOG = LoggerFactory.getLogger(getClass());
    @Resource
    BpmBusDataHandle bpmBusDataHandle;
    @Resource
    BpmBusLinkManager bpmBusLinkManager;
    @Resource
    BpmDefinitionManager bpmDefinitionManager;
    @Resource
    BpmInstanceManager bpmIntanceManager;
    @Resource
    BpmProcessDefService bpmProcessDefService;
    @Resource
    BpmRightsFormService bpmRightsFormService;
    @Resource
    IBusinessDataService businessDataService;
    @Resource
    FormService formService;
    @Resource
    IGroovyScriptEngine iGroovyScriptEngine;
    @Resource
    BpmTaskManager taskManager;

    public BpmFlowInstanceData getInstanceData(String instanceId, FormType formType, String nodeId) {
        BpmContext.cleanTread();
        BpmFlowInstanceData data = new BpmFlowInstanceData();
        data.setInstance((BpmInstance) this.bpmIntanceManager.get(instanceId));
        getInstanceFormData(data, instanceId, nodeId, formType, true);
        handelBtns(data, nodeId, true);
        return data;
    }

    public BpmFlowData getStartFlowData(String defId, String instanceId, FormType formType, Boolean readonly) {
        BpmContext.cleanTread();
        if (!StringUtil.isEmpty(instanceId) || !StringUtil.isEmpty(defId)) {
            BpmFlowInstanceData data = new BpmFlowInstanceData();
            if (StringUtil.isEmpty(instanceId)) {
                data.setDefId(defId);
                getStartFormData(data, formType);
            } else {
                BpmInstance instance = (BpmInstance) this.bpmIntanceManager.get(instanceId);
                data.setInstance(instance);
                getInstanceFormData(data, instanceId, readonly.booleanValue() ? "" : this.bpmProcessDefService.getStartEvent(instance.getDefId()).getNodeId(), formType, readonly.booleanValue());
            }
            handelBtns(data, this.bpmProcessDefService.getStartEvent(data.getDefId()).getNodeId(), readonly.booleanValue());
            return data;
        }
        throw new BusinessException("获取发起流程数据失败!流程定义id或者实例id缺失", BpmStatusCode.PARAM_ILLEGAL);
    }

    @Override
    public Map<String, IBusinessData> getTaskBusData(String str) {
        //TODO unsupport
        return null;
    }

    public BpmFlowData getFlowTaskData(String taskId, FormType formType) {
        BpmContext.cleanTread();
        BpmFlowTaskData taskData = new BpmFlowTaskData();
        IBpmTask task = (IBpmTask) this.taskManager.get(taskId);
        if (task == null) {
            throw new BusinessMessage("任务可能已经办理完成", BpmStatusCode.TASK_NOT_FOUND);
        }
        taskData.setTask(task);
        getInstanceFormData(taskData, task.getInstId(), task.getNodeId(), formType, false);
        handelBtns(taskData, task.getNodeId(), false);
        return taskData;
    }

    private void getStartFormData(BpmFlowInstanceData flowData, FormType formType) {
        String defId = flowData.getDefId();
        BpmNodeDef startNode = this.bpmProcessDefService.getStartEvent(defId);
        flowData.setDefName(this.bpmProcessDefService.getBpmProcessDef(defId).getName());
        IBusinessPermission permission = this.bpmRightsFormService.getInstanceFormPermission(flowData, startNode.getNodeId(), formType, false);
        BpmForm form = flowData.getForm();
        if (FormCategory.INNER.equals(form.getType())) {
            Map<String, IBusinessData> dataMap = this.bpmBusDataHandle.getInitData(permission, defId);
            IFormDef formDef = this.formService.getByFormKey(form.getFormValue());
            if (formDef == null) {
                throw new BusinessException(form.getFormValue(), BpmStatusCode.FLOW_FORM_LOSE);
            }
            form.setFormHtml(formDef.getHtml());
            flowData.setDataMap(dataMap);
            handleShowJsonData(flowData, startNode.getNodeId());
            return;
        }
        form.setFormValue(form.getFormValue().replace("{bizId}", ""));
    }

    private void getInstanceFormData(BpmFlowData flowData, String instaneId, String nodeId, FormType formType, boolean isReadOnly) {
        BpmInstance instance = (BpmInstance) this.bpmIntanceManager.get(instaneId);
        IBusinessPermission businessPermision = this.bpmRightsFormService.getInstanceFormPermission(flowData, nodeId, formType, isReadOnly);
        BpmForm form = flowData.getForm();
        if (FormCategory.INNER.equals(form.getType())) {
            flowData.setDataMap(this.bpmBusDataHandle.getInstanceData(businessPermision, instance));
            IFormDef formDef = this.formService.getByFormKey(form.getFormValue());
            if (formDef == null) {
                throw new BusinessException(form.getFormValue(), BpmStatusCode.FLOW_FORM_LOSE);
            }
            form.setFormHtml(formDef.getHtml());
            handleShowJsonData(flowData, nodeId);
        }
        HandelFormUrl(form, instance);
    }

    private void handleShowJsonData(BpmFlowData flowData, String nodeId) {
        Map<String, IBusinessData> bos = flowData.getDataMap();
        if (!MapUtil.isEmpty(bos)) {
            DefaultBpmProcessDef def = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(flowData.getDefId());
            Map<String, Object> param = new HashMap<>();
            param.putAll(bos);
            if (flowData instanceof BpmFlowTaskData) {
                param.put("task", ((BpmFlowTaskData) flowData).getTask());
            } else if (flowData instanceof BpmFlowInstanceData) {
                param.put("instance", ((BpmFlowInstanceData) flowData).getInstance());
            }
            for (NodeInit init : def.getNodeInitList(nodeId)) {
                if (StringUtil.isNotEmpty(init.getBeforeShow())) {
                    try {
                        this.iGroovyScriptEngine.execute(init.getBeforeShow(), param);
                        this.LOG.debug("执行节点数据初始化脚本{}", init.getBeforeShow());
                    } catch (Exception e) {
                        throw new BusinessError("执行脚本初始化失败", BpmStatusCode.FLOW_DATA_EXECUTE_SHOWSCRIPT_ERROR, e);
                    }
                }
            }
            JSONObject json = new JSONObject();
            JSONObject initJson = new JSONObject();
            for (String key : bos.keySet()) {
                IBusinessData bd = (IBusinessData) bos.get(key);
                json.put(key, this.businessDataService.assemblyFormDefData(bd));
                bd.fullBusDataInitData(initJson);
            }
            flowData.setData(json);
            flowData.setInitData(initJson);
        }
    }

    private void handelBtns(BpmFlowData flowData, String nodeId, boolean isReadOnly) {
        List<Button> buttons = this.bpmProcessDefService.getBpmNodeDef(flowData.getDefId(), nodeId).getButtons();
        if (isReadOnly) {
            buttons = ButtonFactory.getInstanceButtons();
        }
        Map<String, Object> param = new HashMap<>();
        if (MapUtil.isNotEmpty(flowData.getDataMap())) {
            param.putAll(flowData.getDataMap());
        }
        if (flowData instanceof BpmFlowTaskData) {
            IBpmTask task = ((BpmFlowTaskData) flowData).getTask();
            param.put("task", task);
            param.put("bpmTask", task);
        } else if (flowData instanceof BpmFlowInstanceData) {
            param.put("instance", ((BpmFlowInstanceData) flowData).getInstance());
            param.put("bpmInstance", ((BpmFlowInstanceData) flowData).getInstance());
        }
        List<Button> btns = new ArrayList<>();
        for (Button btn : buttons) {
            if (StringUtil.isNotEmpty(btn.getGroovyScript())) {
                try {
                    boolean result = this.iGroovyScriptEngine.executeBoolean(btn.getGroovyScript(), param);
                    this.LOG.debug("任务节点按钮Groovy脚本{},执行结果{}", btn.getGroovyScript(), Boolean.valueOf(result));
                    if (!result) {
                    }
                } catch (Exception e) {
                    throw new BusinessError("按钮脚本执行失败，脚本：" + btn.getGroovyScript(), BpmStatusCode.FLOW_DATA_GET_BUTTONS_ERROR, e);
                }
            }
            btns.add(btn);
        }
        flowData.setButtonList(ButtonFactory.specialTaskBtnHandler(btns, flowData));
    }

    private void HandelFormUrl(BpmForm form, IBpmInstance instance) {
        if (form != null && !form.isFormEmpty() && FormCategory.INNER != form.getType()) {
            form.setFormValue(form.getFormValue().replace("{bizId}", instance.getBizKey()));
        }
    }
}
