package org.openbpm.bpm.plugin.node.ruleskip.context;

import com.alibaba.fastjson.JSON;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.engine.plugin.runtime.RunTimePlugin;
import org.openbpm.bpm.engine.plugin.context.AbstractBpmPluginContext;
import org.openbpm.bpm.plugin.node.ruleskip.def.JumpRule;
import org.openbpm.bpm.plugin.node.ruleskip.def.RuleSkipPluginDef;
import org.openbpm.bpm.plugin.node.ruleskip.executer.RuleSkipPluginExecutor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class RuleSkipPluginContext extends AbstractBpmPluginContext<RuleSkipPluginDef> {
    private static final long serialVersionUID = 8784633971785686365L;

    public List getEventTypes() {
        List<EventType> eventTypes = new ArrayList<>();
        eventTypes.add(EventType.TASK_PRE_COMPLETE_EVENT);
        return eventTypes;
    }

    public Class<? extends RunTimePlugin> getPluginClass() {
        return RuleSkipPluginExecutor.class;
    }

    public String getTitle() {
        return "规则跳转";
    }

    protected RuleSkipPluginDef parseFromJson(JSON json) {
        RuleSkipPluginDef def = new RuleSkipPluginDef();
        def.setJumpRules(JSON.parseArray(json.toJSONString(), JumpRule.class));
        return def;
    }
}
