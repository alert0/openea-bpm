package org.openbpm.bpm.engine.plugin.factory;

import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.engine.action.cmd.ActionCmd;
import org.openbpm.bpm.api.engine.action.cmd.InstanceActionCmd;
import org.openbpm.bpm.api.engine.action.cmd.TaskActionCmd;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.engine.action.cmd.DefaultInstanceActionCmd;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.openbpm.bpm.engine.plugin.session.BpmExecutionPluginSession;
import org.openbpm.bpm.engine.plugin.session.BpmPluginSession;
import org.openbpm.bpm.engine.plugin.session.BpmUserCalcPluginSession;
import org.openbpm.bpm.engine.plugin.session.impl.DefaultBpmExecutionPluginSession;
import org.openbpm.bpm.engine.plugin.session.impl.DefaultBpmTaskPluginSession;
import org.openbpm.bpm.engine.plugin.session.impl.DefaultBpmUserCalcPluginSession;

public class BpmPluginSessionFactory {
    public static BpmExecutionPluginSession buildExecutionPluginSession(InstanceActionCmd actionModel, EventType eventType) {
        DefaultBpmExecutionPluginSession executionPluginSession = new DefaultBpmExecutionPluginSession();
        executionPluginSession.setBoDatas(actionModel.getBizDataMap());
        executionPluginSession.setBpmInstance(actionModel.getBpmInstance());
        executionPluginSession.setEventType(eventType);
        executionPluginSession.setVariableScope(((DefaultInstanceActionCmd) actionModel).getExecutionEntity());
        return executionPluginSession;
    }

    public static BpmExecutionPluginSession buildTaskPluginSession(TaskActionCmd actionModel, EventType eventType) {
        DefualtTaskActionCmd taskActionModel = (DefualtTaskActionCmd) actionModel;
        DefaultBpmTaskPluginSession session = new DefaultBpmTaskPluginSession();
        session.setBoDatas(actionModel.getBizDataMap());
        session.setBpmInstance(actionModel.getBpmInstance());
        session.setEventType(eventType);
        session.setVariableScope(taskActionModel.getDelagateTask());
        session.setBpmTask(taskActionModel.getBpmTask());
        return session;
    }

    public static DefaultBpmExecutionPluginSession buildExecutionPluginSession(TaskActionCmd actionModel, EventType eventType) {
        DefualtTaskActionCmd taskActionModel = (DefualtTaskActionCmd) actionModel;
        DefaultBpmExecutionPluginSession executionPluginSession = new DefaultBpmExecutionPluginSession();
        executionPluginSession.setBoDatas(actionModel.getBizDataMap());
        executionPluginSession.setBpmInstance(actionModel.getBpmInstance());
        executionPluginSession.setVariableScope(taskActionModel.getDelagateTask());
        executionPluginSession.setEventType(eventType);
        return executionPluginSession;
    }

    public static BpmUserCalcPluginSession buildBpmUserCalcPluginSession(BpmPluginSession pluginSession) {
        DefaultBpmExecutionPluginSession plugin = (DefaultBpmExecutionPluginSession) pluginSession;
        DefaultBpmUserCalcPluginSession userCalcPluginSession = new DefaultBpmUserCalcPluginSession();
        userCalcPluginSession.setBoDatas(pluginSession.getBoDatas());
        userCalcPluginSession.setVariableScope(plugin.getVariableScope());
        if (pluginSession instanceof DefaultBpmTaskPluginSession) {
            userCalcPluginSession.setBpmTask(((DefaultBpmTaskPluginSession) pluginSession).getBpmTask());
        }
        ActionCmd action = BpmContext.getActionModel();
        if (action != null && (action instanceof TaskActionCmd)) {
            userCalcPluginSession.setBpmTask(((TaskActionCmd) action).getBpmTask());
        }
        return userCalcPluginSession;
    }
}
