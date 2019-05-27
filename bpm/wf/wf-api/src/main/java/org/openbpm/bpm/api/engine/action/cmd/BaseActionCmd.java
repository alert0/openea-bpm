package org.openbpm.bpm.api.engine.action.cmd;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.engine.action.handler.ActionHandler;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.model.def.IBpmDefinition;
import org.openbpm.bpm.api.model.inst.BpmExecutionStack;
import org.openbpm.bpm.api.model.inst.IBpmInstance;
import org.openbpm.bus.api.model.IBusinessData;
import org.openbpm.form.api.model.FormCategory;
import org.openbpm.org.api.service.UserService;
import org.openbpm.sys.api.model.SysIdentity;
import org.openbpm.sys.util.ContextUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class BaseActionCmd implements ActionCmd {
    private String actionName;
    protected Map<String, IBusinessData> bizDataMap;
    protected IBpmDefinition bpmDefinition;
    protected IBpmInstance bpmInstance;
    protected JSONObject busData;
    private String businessKey;
    private String dataMode;
    protected String defId;
    protected String destination;
    private BpmExecutionStack executionStack;
    protected JSONObject extendConf;
    protected String formId;
    protected boolean hasExecuted;
    protected Map<String, List<SysIdentity>> identityMap_;
    protected String instanceId;
    protected boolean isSource;
    private String opinion;
    protected Map<String, Object> variable_;

    public abstract String getNodeId();

    public abstract void initSpecialParam(FlowRequestParam flowRequestParam);

    public BaseActionCmd() {
        this.variable_ = new HashMap();
        this.identityMap_ = new HashMap();
        this.bpmDefinition = null;
        this.bpmInstance = null;
        this.bizDataMap = new HashMap();
        this.isSource = false;
        this.hasExecuted = false;
    }

    public BaseActionCmd(FlowRequestParam flowParam) {
        this.variable_ = new HashMap();
        this.identityMap_ = new HashMap();
        this.bpmDefinition = null;
        this.bpmInstance = null;
        this.bizDataMap = new HashMap();
        this.isSource = false;
        this.hasExecuted = false;
        this.isSource = true;
        setActionName(flowParam.getAction());
        setDefId(flowParam.getDefId());
        setInstanceId(flowParam.getInstanceId());
        setBusinessKey(flowParam.getBusinessKey());
        initSpecialParam(flowParam);
        if (CollectionUtil.isNotEmpty(flowParam.getNodeUsers())) {
            handleUserSetting(flowParam.getNodeUsers());
        }
        setBusData(flowParam.getData());
        String formType = FormCategory.INNER.value();
        if (StringUtil.isNotEmpty(flowParam.getFormType())) {
            formType = flowParam.getFormType();
        }
        if (FormCategory.INNER.value().equals(formType)) {
            setDataMode(ActionCmd.DATA_MODE_BO);
        } else {
            setDataMode(ActionCmd.DATA_MODE_PK);
        }
        setExtendConf(flowParam.getExtendConf());
        setOpinion(flowParam.getOpinion());
    }

    private void handleUserSetting(JSONObject jsonObject) {
        if (!jsonObject.isEmpty()) {
            Map<String, List<SysIdentity>> map = new HashMap<>();
            for (String nodeId : jsonObject.keySet()) {
                JSONArray users = jsonObject.getJSONArray(nodeId);
                if (users != null && !users.isEmpty()) {
                    List<SysIdentity> userList = new ArrayList<>();
                    Iterator it = users.iterator();
                    while (it.hasNext()) {
                        SysIdentity bpmInentity = (SysIdentity) JSON.toJavaObject((JSONObject) it.next(), SysIdentity.class);
                        if (!StringUtil.isEmpty(bpmInentity.getId())) {
                            userList.add(bpmInentity);
                        }
                    }
                    map.put(nodeId, userList);
                }
            }
            setBpmIdentities(map);
        }
    }

    public Map<String, Object> getActionVariables() {
        return this.variable_;
    }

    public void setActionVariables(Map<String, Object> variables) {
        this.variable_ = variables;
    }

    public void setBpmIdentities(Map<String, List<SysIdentity>> map) {
        this.identityMap_ = map;
    }

    public void clearBpmIdentities() {
        this.identityMap_.clear();
    }

    public void addBpmIdentity(String key, SysIdentity bpmIdentity) {
        List<SysIdentity> list = (List) this.identityMap_.get(key);
        if (CollectionUtil.isEmpty(list)) {
            List<SysIdentity> list2 = new ArrayList<>();
            list2.add(bpmIdentity);
            this.identityMap_.put(key, list2);
            return;
        }
        list.add(bpmIdentity);
    }

    public void addBpmIdentity(String key, List<SysIdentity> bpmIdentityList) {
        List<SysIdentity> list = (List) this.identityMap_.get(key);
        if (CollectionUtil.isEmpty(list)) {
            List<SysIdentity> list2 = new ArrayList<>();
            list2.addAll(bpmIdentityList);
            this.identityMap_.put(key, list2);
            return;
        }
        list.addAll(bpmIdentityList);
    }

    public void setBpmIdentity(String key, List<SysIdentity> bpmIdentityList) {
        List<SysIdentity> list = (List) this.identityMap_.get(key);
        if (CollectionUtil.isEmpty(list)) {
            List<SysIdentity> list2 = new ArrayList<>();
            list2.addAll(bpmIdentityList);
            this.identityMap_.put(key, list2);
            return;
        }
        list.clear();
        list.addAll(bpmIdentityList);
    }

    public List<SysIdentity> getBpmIdentity(String nodeId) {
        return (List) this.identityMap_.get(nodeId);
    }

    public Map<String, List<SysIdentity>> getBpmIdentities() {
        return this.identityMap_;
    }

    public boolean isSource() {
        return this.isSource;
    }

    public void setSource(boolean isSource2) {
        this.isSource = isSource2;
    }

    public JSONObject getBusData() {
        return this.busData;
    }

    public void setBusData(JSONObject busData2) {
        this.busData = busData2;
    }

    public IBpmInstance getBpmInstance() {
        return this.bpmInstance;
    }

    public void setBpmInstance(IBpmInstance bpmInstance2) {
        this.bpmInstance = bpmInstance2;
    }

    public String getDataMode() {
        return this.dataMode;
    }

    public void setDataMode(String mode) {
        this.dataMode = mode;
    }

    public String getBusinessKey() {
        return this.businessKey;
    }

    public String getFormId() {
        return this.formId;
    }

    public BpmExecutionStack getExecutionStack() {
        return this.executionStack;
    }

    public void setExecutionStack(BpmExecutionStack executionStack2) {
        this.executionStack = executionStack2;
    }

    public String getInstanceId() {
        if (!StringUtil.isEmpty(this.instanceId) || this.bpmInstance == null) {
            return this.instanceId;
        }
        return this.bpmInstance.getId();
    }

    public void setInstanceId(String instanceId2) {
        this.instanceId = instanceId2;
    }

    public String getDefId() {
        if (!StringUtil.isEmpty(this.defId) || this.bpmInstance == null) {
            return this.defId;
        }
        return this.bpmInstance.getDefId();
    }

    public JSONObject getExtendConf() {
        return this.extendConf;
    }

    public void setExtendConf(JSONObject extendConf2) {
        this.extendConf = extendConf2;
    }

    public void setDefId(String defId2) {
        this.defId = defId2;
    }

    public String getOpinion() {
        return this.opinion;
    }

    public void setOpinion(String opinion2) {
        this.opinion = opinion2;
    }

    public Map<String, IBusinessData> getBizDataMap() {
        return this.bizDataMap;
    }

    public void setBizDataMap(Map<String, IBusinessData> bizDataMap2) {
        this.bizDataMap = bizDataMap2;
    }

    public void setFormId(String formId2) {
        this.formId = formId2;
    }

    public void setBusinessKey(String businessKey2) {
        this.businessKey = businessKey2;
    }

    public void setCurAccount(String curAccount) {
        ContextUtil.setCurrentUser(((UserService) AppUtil.getBean(UserService.class)).getUserByAccount(curAccount));
    }

    public String getActionName() {
        return this.actionName;
    }

    public ActionType getActionType() {
        return ActionType.fromKey(getActionName());
    }

    public void setActionName(String actionName2) {
        this.actionName = actionName2;
    }

    public IBpmDefinition getBpmDefinition() {
        return this.bpmDefinition;
    }

    public void setBpmDefinition(IBpmDefinition bpmDefinition2) {
        this.bpmDefinition = bpmDefinition2;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination2) {
        this.destination = destination2;
    }

    public synchronized String executeCmd() {
        ActionHandler handler;
        if (this.hasExecuted) {
            throw new BusinessException("action cmd caonot be invoked twice", BpmStatusCode.NO_PERMISSION);
        }
        this.hasExecuted = true;
        ActionType actonType = ActionType.fromKey(getActionName());
        handler = (ActionHandler) AppUtil.getBean(actonType.getBeanId());
        if (handler == null) {
            throw new BusinessException("action beanId cannot be found :" + actonType.getName(), BpmStatusCode.NO_TASK_ACTION);
        }
        BpmContext.cleanTread();
        handler.execute(this);
        return handler.getActionType().getName();
    }
}
