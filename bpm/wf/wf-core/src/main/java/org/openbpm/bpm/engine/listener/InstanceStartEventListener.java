package org.openbpm.bpm.engine.listener;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.constant.ScriptType;
import org.openbpm.bpm.api.engine.action.cmd.ActionCmd;
import org.openbpm.bpm.api.engine.action.cmd.InstanceActionCmd;
import org.openbpm.bpm.api.engine.action.cmd.TaskActionCmd;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.bpm.core.manager.BpmDefinitionManager;
import org.openbpm.bpm.core.manager.BpmInstanceManager;
import org.openbpm.bpm.core.manager.BpmTaskOpinionManager;
import org.openbpm.bpm.core.model.BpmDefinition;
import org.openbpm.bpm.core.model.BpmInstance;
import org.openbpm.bpm.engine.action.cmd.DefaultInstanceActionCmd;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import org.openbpm.bus.api.model.IBusinessData;
import org.openbpm.sys.util.ContextUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class InstanceStartEventListener extends AbstractInstanceListener {
    @Resource
    BpmDefinitionManager bpmDefinitionMananger;
    @Resource
    BpmInstanceManager bpmInstanceManager;
    @Resource
    BpmProcessDefService bpmProcessDefService;
    @Resource
    BpmTaskOpinionManager bpmTaskOpinionManager;

    public EventType getBeforeTriggerEventType() {
        return EventType.START_EVENT;
    }

    public EventType getAfterTriggerEventType() {
        return EventType.START_POST_EVENT;
    }

    public void beforePluginExecute(InstanceActionCmd instanceActionModel) {
        this.LOG.debug("流程实例【{}】执行启动过程 instanceID:[{}]", instanceActionModel.getBpmInstance().getSubject(), instanceActionModel.getBpmInstance().getId());
        Map<String, Object> actionVariables = instanceActionModel.getActionVariables();
        if (!CollectionUtil.isEmpty(actionVariables)) {
            for (String key : actionVariables.keySet()) {
                instanceActionModel.addVariable(key, actionVariables.get(key));
            }
            this.LOG.debug("设置流程变量【{}】", actionVariables.keySet().toString());
        }
    }

    public void triggerExecute(InstanceActionCmd instanceActionModel) {
        this.bpmTaskOpinionManager.createOpinionByInstance(instanceActionModel, true);
        handleInstanceSubject((DefaultInstanceActionCmd) instanceActionModel);
    }

    public void afterPluginExecute(InstanceActionCmd instanceActionModel) {
        this.LOG.debug("流程实例【{}】完成创建过程   instanceID：{}", instanceActionModel.getBpmInstance().getSubject(), instanceActionModel.getBpmInstance().getId());
    }

    protected ScriptType getScriptType() {
        return ScriptType.START;
    }

    protected InstanceActionCmd getInstanceActionModel(ExecutionEntity excutionEntity) {
        handlerSubProcess(excutionEntity, BpmContext.getActionModel());
        DefaultInstanceActionCmd actionModel = (DefaultInstanceActionCmd) BpmContext.getActionModel();
        actionModel.setExecutionEntity(excutionEntity);
        BpmInstance instance = (BpmInstance) actionModel.getBpmInstance();
        if (StringUtil.isEmpty(instance.getActInstId())) {
            instance.setActDefId(excutionEntity.getProcessDefinitionId());
            instance.setActInstId(excutionEntity.getProcessInstanceId());
        }
        return actionModel;
    }

    private void handleInstanceSubject(DefaultInstanceActionCmd data) {
        BpmInstance instance = (BpmInstance) data.getBpmInstance();
        DefaultBpmProcessDef processDef = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(instance.getDefId());
        String subjectRule = processDef.getExtProperties().getSubjectRule();
        if (!StringUtil.isEmpty(subjectRule)) {
            Map<String, Object> ruleVariables = new HashMap<>();
            ruleVariables.put("title", processDef.getName());
            ruleVariables.put("startorName", ContextUtil.getCurrentUser().getFullname());
            ruleVariables.put("startDate", DateUtil.format(new Date(), "yyyy-MM-dd"));
            ruleVariables.put("startTime", DateUtil.now());
            ruleVariables.putAll(data.getVariables());
            Map<String, IBusinessData> boMap = data.getBizDataMap();
            if (CollectionUtil.isNotEmpty(boMap)) {
                for (String bocode : boMap.keySet()) {
                    for (Entry<String, Object> entry : ((IBusinessData) boMap.get(bocode)).getData().entrySet()) {
                        ruleVariables.put(bocode + "." + ((String) entry.getKey()), entry.getValue());
                    }
                }
            }
            String subject = getTitleByVariables(subjectRule, ruleVariables);
            instance.setSubject(subject);
            this.LOG.debug("更新流程标题:{}", subject);
        }
    }

    private String getTitleByVariables(String subject, Map<String, Object> variables) {
        String name;
        if (StringUtils.isEmpty(subject)) {
            String str = subject;
            return "";
        }
        Matcher matcher = Pattern.compile("\\{(.*?)\\}", 98).matcher(subject);
        while (matcher.find()) {
            String tag = matcher.group(0);
            String rule = matcher.group(1);
            String[] aryRule = rule.split(":");
            String str2 = "";
            if (aryRule.length == 1) {
                name = rule;
            } else {
                name = aryRule[1];
            }
            if (variables.containsKey(name)) {
                Object obj = variables.get(name);
                if (obj != null) {
                    try {
                        subject = subject.replace(tag, obj.toString());
                    } catch (Exception e) {
                        subject = subject.replace(tag, "");
                    }
                } else {
                    subject = subject.replace(tag, "");
                }
            } else {
                subject = subject.replace(tag, "");
            }
        }
        String str3 = subject;
        return subject;
    }

    private void handlerSubProcess(ExecutionEntity excutionEntity, ActionCmd preAction) {
        String preActionDefKey = preAction.getBpmInstance().getDefKey();
        if (!(preAction instanceof InstanceActionCmd)) {
            ExecutionEntity callActivityNode = excutionEntity.getSuperExecution();
            if (!(preAction instanceof TaskActionCmd) || (callActivityNode != null && preAction.getBpmInstance().getActInstId().equals(callActivityNode.getProcessInstanceId()))) {
                BpmDefinition subDefinition = this.bpmDefinitionMananger.getByKey(excutionEntity.getProcessDefinitionKey());
                BpmInstance subInstance = this.bpmInstanceManager.genInstanceByDefinition(subDefinition);
                subInstance.setActInstId(excutionEntity.getProcessInstanceId());
                subInstance.setParentInstId(preAction.getBpmInstance().getId());
                this.bpmInstanceManager.create(subInstance);
                DefaultInstanceActionCmd startAction = new DefaultInstanceActionCmd();
                startAction.setBpmDefinition(subDefinition);
                startAction.setBpmInstance(subInstance);
                startAction.setBizDataMap(preAction.getBizDataMap());
                BpmContext.setActionModel(startAction);
                return;
            }
            throw new BusinessException(BpmStatusCode.ACTIONCMD_ERROR);
        } else if (!excutionEntity.getProcessDefinitionKey().equals(preActionDefKey)) {
            throw new BusinessException("流程启动失败，错误的线程数据！", BpmStatusCode.ACTIONCMD_ERROR);
        }
    }
}
