package org.openbpm.bpm.plugin.global.nodemessage.executer;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.engine.action.cmd.BaseActionCmd;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.engine.plugin.def.UserAssignRule;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.bpm.engine.plugin.factory.BpmPluginSessionFactory;
import org.openbpm.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import org.openbpm.bpm.engine.plugin.session.BpmExecutionPluginSession;
import org.openbpm.bpm.plugin.global.nodemessage.def.NodeMessage;
import org.openbpm.bpm.plugin.global.nodemessage.def.NodeMessagePluginDef;
import org.openbpm.bpm.plugin.node.userassign.executer.UserAssignRuleCalc;
import org.openbpm.sys.api.freemark.IFreemarkerEngine;
import org.openbpm.sys.api.groovy.IGroovyScriptEngine;
import org.openbpm.sys.api.jms.model.DefaultJmsDTO;
import org.openbpm.sys.api.jms.model.JmsDTO;
import org.openbpm.sys.api.jms.model.msg.NotifyMessage;
import org.openbpm.sys.api.jms.producer.JmsProducer;
import org.openbpm.sys.api.model.SysIdentity;
import org.openbpm.sys.util.ContextUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NodeMessagePluginExecutor extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, NodeMessagePluginDef> {
    @Autowired
    IFreemarkerEngine freemarkEngine;
    @Resource
    JmsProducer jmsProducer;
    @Resource
    BpmProcessDefService processDefService;
    @Resource
    IGroovyScriptEngine scriptEngine;

    public Void execute(BpmExecutionPluginSession pluginSession, NodeMessagePluginDef pluginDef) {
        for (NodeMessage nodeMessage : pluginDef.getNodeMessageList()) {
            if (supportCondition(pluginSession, nodeMessage)) {
                this.jmsProducer.sendToQueue(getJmsMsgVo(nodeMessage, pluginSession));
                this.LOG.debug("【节点消息插件】发送消息成功！时机：{}，消息标题：{}", pluginSession.getEventType().getValue(), nodeMessage.getDesc());
            }
        }
        return null;
    }

    private List<JmsDTO> getJmsMsgVo(NodeMessage nodeMessage, BpmExecutionPluginSession session) {
        List<SysIdentity> userList;
        String[] msgType = nodeMessage.getMsgType().split(",");
        String htmlTemplate = nodeMessage.getHtmlTemplate();
        String textTemplate = nodeMessage.getTextTemplate();
        try {
            if (StringUtil.isNotEmpty(htmlTemplate)) {
                htmlTemplate = this.freemarkEngine.parseByString(htmlTemplate.replaceAll("&lt;", "<").replaceAll("&gt;", ">"), session);
            }
            if (StringUtil.isNotEmpty(textTemplate)) {
                String textTemplate2 = this.freemarkEngine.parseByString(textTemplate, session);
            }
        } catch (Exception e) {
            this.LOG.error("htmlTemplate:{};textTempalte:{}", htmlTemplate, textTemplate);
            this.LOG.error("instId[{}]消息发送插件解析消息模板失败，可能原因为:{}", new Object[]{session.getBpmInstance().getId(), e.getMessage(), e});
            e.printStackTrace();
        }
        new ArrayList();
        if (CollectionUtil.isEmpty(nodeMessage.getUserRules())) {
            BaseActionCmd cmd = (BaseActionCmd) BpmContext.getActionModel();
            userList = cmd.getBpmIdentity(cmd.getNodeId());
        } else {
            userList = getUser(session, nodeMessage.getUserRules());
        }
        if (CollectionUtil.isEmpty(userList)) {
            this.LOG.debug("【节点消息插件】没有需要发送的消息！原因：接收消息人员为空。 节点：{}，时机：{}，消息标题：{}", new Object[]{getActivitiId(session), session.getEventType().getValue(), nodeMessage.getDesc()});
            return Collections.emptyList();
        }
        List<JmsDTO> jmsDto = new ArrayList<>();
        for (String type : msgType) {
            jmsDto.add(new DefaultJmsDTO(type, new NotifyMessage(nodeMessage.getDesc(), htmlTemplate, ContextUtil.getCurrentUser(), userList)));
        }
        return jmsDto;
    }

    private List<SysIdentity> getUser(BpmExecutionPluginSession pluginSession, List<UserAssignRule> ruleList) {
        return UserAssignRuleCalc.calcUserAssign(BpmPluginSessionFactory.buildBpmUserCalcPluginSession(pluginSession), ruleList, Boolean.valueOf(false));
    }

    private boolean supportCondition(BpmExecutionPluginSession session, NodeMessage nodeMessage) {
        if (StringUtil.isNotEmpty(nodeMessage.getEvent()) && !nodeMessage.getEvent().equals(session.getEventType().getKey())) {
            return false;
        }
        if (StringUtil.isNotEmpty(nodeMessage.getNodeId()) && !nodeMessage.getNodeId().equals(getActivitiId(session))) {
            return false;
        }
        if (!StringUtil.isNotEmpty(nodeMessage.getCondition()) || Boolean.valueOf(this.scriptEngine.executeBoolean(nodeMessage.getCondition(), session)).booleanValue()) {
            return true;
        }
        return false;
    }
}
