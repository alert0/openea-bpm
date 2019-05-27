package org.openbpm.bpm.plugin.usercalc.approver.executer;

import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.model.task.IBpmTaskOpinion;
import org.openbpm.bpm.core.manager.BpmTaskOpinionManager;
import org.openbpm.bpm.engine.model.BpmIdentity;
import org.openbpm.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
import org.openbpm.bpm.engine.plugin.session.BpmUserCalcPluginSession;
import org.openbpm.bpm.plugin.usercalc.approver.def.ApproverPluginDef;
import org.openbpm.bpm.plugin.usercalc.user.def.ExecutorVar;
import org.openbpm.sys.api.model.SysIdentity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ApproverPluginExecutor extends AbstractUserCalcPlugin<ApproverPluginDef> {
    @Resource
    BpmTaskOpinionManager taskOpinionManager;

    public List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, ApproverPluginDef pluginDef) {
        List<SysIdentity> bpmIdentities = new ArrayList<>();
        Iterator it = this.taskOpinionManager.getByInstId(pluginSession.getBpmTask().getInstId()).iterator();
        while (it.hasNext()) {
            IBpmTaskOpinion taskOpinion = (IBpmTaskOpinion) it.next();
            if (!StringUtil.isEmpty(taskOpinion.getApprover())) {
                bpmIdentities.add(new BpmIdentity(taskOpinion.getApprover(), taskOpinion.getApproverName(), ExecutorVar.EXECUTOR_TYPE_USER));
            }
        }
        return bpmIdentities;
    }
}
