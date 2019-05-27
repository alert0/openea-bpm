package org.openbpm.bpm.plugin.global.datalog.context;

import com.alibaba.fastjson.JSON;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.engine.plugin.runtime.RunTimePlugin;
import org.openbpm.bpm.engine.plugin.context.AbstractBpmPluginContext;
import org.openbpm.bpm.plugin.global.datalog.def.DataLogPluginDef;
import org.openbpm.bpm.plugin.global.datalog.executer.DataLogPluginExecutor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component("dataLogPluginContext")
public class DataLogPluginContext extends AbstractBpmPluginContext<DataLogPluginDef> {
    private static final long serialVersionUID = -1563849340056571771L;

    public List getEventTypes() {
        List<EventType> eventTypes = new ArrayList<>();
        eventTypes.add(EventType.TASK_PRE_COMPLETE_EVENT);
        eventTypes.add(EventType.START_EVENT);
        return eventTypes;
    }

    public Class<? extends RunTimePlugin> getPluginClass() {
        return DataLogPluginExecutor.class;
    }

    public String getTitle() {
        return "BO 数据提交日志";
    }

    protected DataLogPluginDef parseFromJson(JSON json) {
        return (DataLogPluginDef) JSON.toJavaObject(json, DataLogPluginDef.class);
    }
}
