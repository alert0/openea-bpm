package org.openbpm.bpm.plugin.node.userassign.context;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.base.core.util.JsonUtil;
import org.openbpm.base.core.util.ThreadMsgUtil;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.engine.plugin.context.UserCalcPluginContext;
import org.openbpm.bpm.api.engine.plugin.context.UserQueryPluginContext;
import org.openbpm.bpm.api.engine.plugin.def.UserAssignRule;
import org.openbpm.bpm.api.engine.plugin.runtime.RunTimePlugin;
import org.openbpm.bpm.engine.plugin.context.AbstractBpmPluginContext;
import org.openbpm.bpm.engine.plugin.context.AbstractUserCalcPluginContext;
import org.openbpm.bpm.plugin.node.userassign.def.UserAssignPluginDef;
import org.openbpm.bpm.plugin.node.userassign.executer.UserAssignPluginExecutor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class UserAssignPluginContext extends AbstractBpmPluginContext<UserAssignPluginDef> implements UserQueryPluginContext {
    public Class getPluginClass() {
        return UserAssignPluginExecutor.class;
    }

    public Class<? extends RunTimePlugin> getUserQueryPluginClass() {
        return UserAssignPluginExecutor.class;
    }

    public List<EventType> getEventTypes() {
        List<EventType> eventTypes = new ArrayList<>();
        eventTypes.add(EventType.TASK_CREATE_EVENT);
        return eventTypes;
    }

    public JSON getJson() {
        if (getBpmPluginDef() == null) {
            return (JSON) JSON.parse("[]");
        }
        List<UserAssignRule> ruleList = getBpmPluginDef().getRuleList();
        if (CollectionUtil.isEmpty(ruleList)) {
            return (JSON) JSON.parse("[]");
        }
        return (JSON) JSON.toJSON(ruleList);
    }

    protected UserAssignPluginDef parseFromJson(JSON pluginJson) {
        JSONArray userRuleList = null;
        int i = 0;
        UserAssignPluginDef def = new UserAssignPluginDef();
        if (pluginJson instanceof JSONObject) {
            JSONObject json = (JSONObject) pluginJson;
            if (!json.containsKey("ruleList")) {
                ThreadMsgUtil.addMsg("人员条件不完整！");
            } else {
                userRuleList = json.getJSONArray("ruleList");
                List<UserAssignRule> ruleList = new ArrayList<>();
                for (i = 0; i < userRuleList.size(); i++) {
                    JSONObject ruleJson = userRuleList.getJSONObject(i);
                    UserAssignRule rule = (UserAssignRule) JSON.toJavaObject(ruleJson, UserAssignRule.class);
                    ruleList.add(rule);
                    if (!ruleJson.containsKey("calcs")) {
                        ThreadMsgUtil.addMsg("人员条件不完整！");
                    } else {
                        JSONArray calcAry = ruleJson.getJSONArray("calcs");
                        List<UserCalcPluginContext> calcPluginContextList = new ArrayList<>();
                        Iterator it = calcAry.iterator();
                        while (it.hasNext()) {
                            JSONObject calcObj = (JSONObject) it.next();
                            String pluginContext = JsonUtil.getString(calcObj, "pluginType") + "PluginContext";
                            AbstractUserCalcPluginContext ctx = (AbstractUserCalcPluginContext) AppUtil.getBean(pluginContext);
                            if (ctx == null) {
                                this.LOG.warn("插件{}查找失败！", pluginContext);
                            } else {
                                ctx.parse(calcObj);
                                calcPluginContextList.add(ctx);
                            }
                        }
                        rule.setCalcPluginContextList(calcPluginContextList);
                    }
                }
                def.setRuleList(ruleList);
            }
        } else {
            userRuleList = (JSONArray) pluginJson;
            List<UserAssignRule> ruleList2 = new ArrayList<>();
            i=0;
            while (i < userRuleList.size()) {
                UserAssignRule rule2 = new UserAssignRule();
                BeanUtil.copyProperties(userRuleList.get(i),rule2);
                ruleList2.add(rule2);
                i++;
            }
            def.setRuleList(ruleList2);
        }
        return def;
    }

    public String getTitle() {
        return "用户分配插件";
    }
}
