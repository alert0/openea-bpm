package org.openbpm.bpm.plugin.global.taskskip.context;

import com.alibaba.fastjson.JSON;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.engine.plugin.runtime.RunTimePlugin;
import org.openbpm.bpm.engine.plugin.context.AbstractBpmPluginContext;
import org.openbpm.bpm.plugin.global.taskskip.def.TaskSkipPluginDef;
import org.openbpm.bpm.plugin.global.taskskip.executer.TaskSkipPluginExecutor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class TaskSkipPluginContext extends AbstractBpmPluginContext<TaskSkipPluginDef> {
    private static final long serialVersionUID = -8171025388788811778L;

    public List<EventType> getEventTypes() {
        List<EventType> list = new ArrayList<>();
        list.add(EventType.TASK_POST_CREATE_EVENT);
        return list;
    }

    public Class<? extends RunTimePlugin> getPluginClass() {
        return TaskSkipPluginExecutor.class;
    }

    protected TaskSkipPluginDef parseFromJson(JSON pluginJson) {
        return (TaskSkipPluginDef) JSON.toJavaObject(pluginJson, TaskSkipPluginDef.class);
    }

    public String getTitle() {
        return "任务跳过";
    }
}
