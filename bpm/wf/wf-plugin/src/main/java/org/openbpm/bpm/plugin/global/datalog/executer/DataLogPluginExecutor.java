package org.openbpm.bpm.plugin.global.datalog.executer;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.bpm.api.engine.action.cmd.BaseActionCmd;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.bpm.engine.action.cmd.DefualtTaskActionCmd;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import org.openbpm.bpm.engine.plugin.runtime.abstact.AbstractBpmExecutionPlugin;
import org.openbpm.bpm.engine.plugin.session.BpmExecutionPluginSession;
import org.openbpm.bpm.plugin.core.manager.BpmSubmitDataLogManager;
import org.openbpm.bpm.plugin.core.model.BpmSubmitDataLog;
import org.openbpm.bpm.plugin.global.datalog.def.DataLogPluginDef;
import org.openbpm.bus.api.service.IBusinessDataService;
import javax.annotation.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class DataLogPluginExecutor extends AbstractBpmExecutionPlugin<BpmExecutionPluginSession, DataLogPluginDef> {
    @Resource
    BpmSubmitDataLogManager bpmSubmitDataLogManager;
    @Resource
    private IBusinessDataService businessDataService;
    @Resource
    BpmProcessDefService processDefService;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public Void execute(BpmExecutionPluginSession pluginSession, DataLogPluginDef pluginDef) {
        BaseActionCmd cmd = (BaseActionCmd) BpmContext.getActionModel();
        JSONObject businessData = cmd.getBusData();
        if (businessData != null && !businessData.isEmpty()) {
            String defId = pluginSession.getBpmInstance().getDefId();
            DefaultBpmProcessDef processDef = (DefaultBpmProcessDef)this.processDefService.getBpmProcessDef(defId);
            if (!processDef.getExtProperties().isLogSubmitData()) {
                return null;
            } else {
                BpmSubmitDataLog bpmSubmitDataLog = new BpmSubmitDataLog();
                bpmSubmitDataLog.setData(businessData.toJSONString());
                bpmSubmitDataLog.setDestination(cmd.getDestination());
                if (cmd.getExtendConf() != null) {
                    bpmSubmitDataLog.setExtendConf(cmd.getExtendConf().toJSONString());
                }
                if (cmd instanceof DefualtTaskActionCmd) {
                    bpmSubmitDataLog.setTaskId(((DefualtTaskActionCmd) cmd).getBpmTask().getId());
                }
                bpmSubmitDataLog.setInstId(cmd.getInstanceId());
                this.threadPoolTaskExecutor.execute(() -> {
                        bpmSubmitDataLogManager.create(bpmSubmitDataLog);
                });
                this.LOG.debug("记录流程提交业务数据 ");
            }
        }
        return null;
    }

}
