package org.openbpm.bpm.plugin.usercalc.samenode.executer;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.bpm.api.model.task.IBpmTaskOpinion;
import org.openbpm.bpm.core.manager.BpmTaskOpinionManager;
import org.openbpm.bpm.core.model.BpmTaskOpinion;
import org.openbpm.bpm.engine.model.BpmIdentity;
import org.openbpm.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
import org.openbpm.bpm.engine.plugin.session.BpmUserCalcPluginSession;
import org.openbpm.bpm.plugin.usercalc.samenode.def.SameNodePluginDef;
import org.openbpm.bpm.plugin.usercalc.user.def.ExecutorVar;
import org.openbpm.sys.api.model.SysIdentity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class SameNodePluginExecutor extends AbstractUserCalcPlugin<SameNodePluginDef> {
    @Resource
    BpmTaskOpinionManager taskOpinionManager;

    public List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, SameNodePluginDef sameNodeDef) {
        List<SysIdentity> bpmIdentities = new ArrayList<>();
        List<BpmTaskOpinion> taskOpinionList = this.taskOpinionManager.getByInstAndNode(pluginSession.getBpmTask().getInstId(), sameNodeDef.getNodeId());
        if (!CollectionUtil.isEmpty(taskOpinionList)) {
            IBpmTaskOpinion taskOpinion = (IBpmTaskOpinion) taskOpinionList.get(taskOpinionList.size() - 1);
            bpmIdentities.add(new BpmIdentity(taskOpinion.getApprover(), taskOpinion.getApproverName(), ExecutorVar.EXECUTOR_TYPE_USER));
        }
        return bpmIdentities;
    }

    public boolean supportPreView() {
        return false;
    }
}
