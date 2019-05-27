package org.openbpm.bpm.plugin.global.nodemessage.context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.engine.plugin.runtime.RunTimePlugin;
import org.openbpm.bpm.engine.plugin.context.AbstractBpmPluginContext;
import org.openbpm.bpm.plugin.global.nodemessage.def.NodeMessage;
import org.openbpm.bpm.plugin.global.nodemessage.def.NodeMessagePluginDef;
import org.openbpm.bpm.plugin.global.nodemessage.executer.NodeMessagePluginExecutor;
import org.openbpm.bpm.plugin.node.userassign.context.UserAssignPluginContext;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class NodeMessagePluginContext extends AbstractBpmPluginContext<NodeMessagePluginDef> {
    private static final long serialVersionUID = -8171025388788811778L;

    public List<EventType> getEventTypes() {
        List<EventType> list = new ArrayList<>();
        list.add(EventType.TASK_POST_CREATE_EVENT);
        list.add(EventType.START_EVENT);
        list.add(EventType.END_EVENT);
        list.add(EventType.TASK_COMPLETE_EVENT);
        return list;
    }

    public Class<? extends RunTimePlugin> getPluginClass() {
        return NodeMessagePluginExecutor.class;
    }

    protected NodeMessagePluginDef parseFromJson(JSON pluginJson) {
        JSONArray array = (JSONArray) pluginJson;
        List<NodeMessage> messageList = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject msgJson = array.getJSONObject(i);
            NodeMessage nodeMessage = (NodeMessage) JSON.toJavaObject(msgJson, NodeMessage.class);
            if (StringUtil.isNotEmpty(msgJson.getString("userRules"))) {
                UserAssignPluginContext userPluginContext = (UserAssignPluginContext) AppUtil.getBean(UserAssignPluginContext.class);
                userPluginContext.parse(msgJson.getString("userRules"));
                nodeMessage.setUserRules(userPluginContext.getBpmPluginDef().getRuleList());
            }
            messageList.add(nodeMessage);
        }
        return new NodeMessagePluginDef(messageList);
    }

    public String getTitle() {
        return "节点消息";
    }
}
