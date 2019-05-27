package org.openbpm.bpm.plugin.global.nodemessage.def;

import org.openbpm.bpm.api.engine.plugin.def.UserAssignRule;
import org.openbpm.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
import java.util.List;
import org.hibernate.validator.constraints.NotBlank;

public class NodeMessage extends AbstractBpmExecutionPluginDef {
    private static final long serialVersionUID = 1;
    private String condition;
    @NotBlank(message = "节点消息描述不能为空")
    private String desc;
    @NotBlank
    private String event;
    private String htmlTemplate;
    @NotBlank
    private String msgType;
    private String nodeId;
    private String textTemplate;
    private List<UserAssignRule> userRules;

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc2) {
        this.desc = desc2;
    }

    public String getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(String nodeId2) {
        this.nodeId = nodeId2;
    }

    public String getEvent() {
        return this.event;
    }

    public void setEvent(String event2) {
        this.event = event2;
    }

    public String getCondition() {
        return this.condition;
    }

    public void setCondition(String condition2) {
        this.condition = condition2;
    }

    public List<UserAssignRule> getUserRules() {
        return this.userRules;
    }

    public void setUserRules(List<UserAssignRule> userRules2) {
        this.userRules = userRules2;
    }

    public String getMsgType() {
        return this.msgType;
    }

    public void setMsgType(String msgType2) {
        this.msgType = msgType2;
    }

    public String getHtmlTemplate() {
        return this.htmlTemplate;
    }

    public void setHtmlTemplate(String htmlTemplate2) {
        this.htmlTemplate = htmlTemplate2;
    }

    public String getTextTemplate() {
        return this.textTemplate;
    }

    public void setTextTemplate(String textTemplate2) {
        this.textTemplate = textTemplate2;
    }
}
