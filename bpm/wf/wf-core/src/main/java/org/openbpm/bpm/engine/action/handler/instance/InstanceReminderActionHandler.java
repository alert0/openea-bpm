package org.openbpm.bpm.engine.action.handler.instance;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.base.api.exception.BusinessMessage;
import org.openbpm.base.core.util.JsonUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.core.manager.BpmTaskManager;
import org.openbpm.bpm.core.model.BpmTask;
import org.openbpm.bpm.engine.action.cmd.DefaultInstanceActionCmd;
import org.openbpm.bpm.engine.action.handler.AbsActionHandler;
import org.openbpm.sys.api.jms.model.DefaultJmsDTO;
import org.openbpm.sys.api.jms.model.JmsDTO;
import org.openbpm.sys.api.jms.model.msg.NotifyMessage;
import org.openbpm.sys.api.jms.producer.JmsProducer;
import org.openbpm.sys.api.model.SysIdentity;
import org.openbpm.sys.util.ContextUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class InstanceReminderActionHandler extends AbsActionHandler<DefaultInstanceActionCmd> {
    @Resource
    JmsProducer jmsProducer;
    @Resource
    BpmTaskManager taskMananger;

    public void execute(DefaultInstanceActionCmd model) {
        String opinion = model.getOpinion();
        Boolean isUrgent = Boolean.valueOf(JsonUtil.getBoolean(model.getExtendConf(), "isUrgent"));
        String msgType = JsonUtil.getString(model.getExtendConf(), "msgType");
        if (StringUtil.isEmpty(opinion)) {
            throw new BusinessMessage("催办提醒内容不可为空！");
        }
        List<BpmTask> taskList = this.taskMananger.getByInstId(model.getInstanceId());
        if (CollectionUtil.isEmpty(taskList)) {
            throw new BusinessMessage("当前实例任务不存在，无需催办！");
        }
        List<SysIdentity> notifyUsers = new ArrayList<>();
        for (BpmTask task : taskList) {
            notifyUsers.addAll(this.taskMananger.getAssignUserById(task));
            if (isUrgent.booleanValue()) {
                task.setPriority(Integer.valueOf(task.getPriority().intValue() + 1));
                this.taskMananger.update(task);
            }
        }
        if (!CollectionUtil.isEmpty(notifyUsers) && !StringUtil.isEmpty(opinion)) {
            String opinion2 = opinion.replaceAll("subject", ((BpmTask) taskList.get(0)).getSubject());
            List<JmsDTO> jmsDto = new ArrayList<>();
            for (String type : msgType.split(",")) {
                jmsDto.add(new DefaultJmsDTO(type, new NotifyMessage("任务催办提醒", opinion2, ContextUtil.getCurrentUser(), notifyUsers)));
            }
            this.jmsProducer.sendToQueue(jmsDto);
        }
    }

    public ActionType getActionType() {
        return ActionType.REMINDER;
    }

    public int getSn() {
        return 7;
    }

    public Boolean isSupport(BpmNodeDef nodeDef) {
        return Boolean.valueOf(false);
    }

    public Boolean isDefault() {
        return Boolean.valueOf(false);
    }

    public String getConfigPage() {
        return "/bpm/task/reminderActionDialog.html";
    }

    public String getDefaultGroovyScript() {
        return "return bpmInstance.getStatus().equals(\"running\");";
    }

    public String getDefaultBeforeScript() {
        return null;
    }

    /* access modifiers changed from: protected */
    public void doAction(DefaultInstanceActionCmd model) {
    }

    /* access modifiers changed from: protected */
    public void prepareActionDatas(DefaultInstanceActionCmd data) {
    }

    /* access modifiers changed from: protected */
    public void doActionBefore(DefaultInstanceActionCmd actionModel) {
    }

    /* access modifiers changed from: protected */
    public void doActionAfter(DefaultInstanceActionCmd actionModel) {
    }
}
