package org.openbpm.bpm.plugin.usercalc.user.executer;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.bpm.core.manager.BpmTaskOpinionManager;
import org.openbpm.bpm.core.model.BpmTaskOpinion;
import org.openbpm.bpm.engine.model.BpmIdentity;
import org.openbpm.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
import org.openbpm.bpm.engine.plugin.session.BpmUserCalcPluginSession;
import org.openbpm.bpm.plugin.usercalc.user.def.ExecutorVar;
import org.openbpm.bpm.plugin.usercalc.user.def.UserPluginDef;
import org.openbpm.org.api.model.IUser;
import org.openbpm.org.api.service.UserService;
import org.openbpm.sys.api.model.SysIdentity;
import org.openbpm.sys.util.ContextUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class UserPluginExecutor extends AbstractUserCalcPlugin<UserPluginDef> {
    @Resource
    UserService UserService;
    @Resource
    BpmProcessDefService bpmProcessDefService;
    @Resource
    BpmTaskOpinionManager taskOpinionManager;

    public List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, UserPluginDef def) {
        String[] aryAccount;
        List<SysIdentity> list = new ArrayList<>();
        String source = def.getSource();
        if ("start".equals(source)) {
            List<BpmTaskOpinion> opinions = this.taskOpinionManager.getByInstAndNode(pluginSession.getBpmTask().getInstId(), this.bpmProcessDefService.getBpmProcessDef(pluginSession.getBpmTask().getDefId()).getStartEvent().getNodeId());
            if (CollectionUtil.isEmpty(opinions)) {
                throw new BusinessException("开始节点意见丢失，无法获取发起人！", BpmStatusCode.USER_CALC_ERROR);
            }
            BpmTaskOpinion firstNode = (BpmTaskOpinion) opinions.get(0);
            list.add(new BpmIdentity(firstNode.getApprover(), firstNode.getApproverName(), ExecutorVar.EXECUTOR_TYPE_USER));
        }
        if ("currentUser".equals(source)) {
            list.add(new BpmIdentity(ContextUtil.getCurrentUser()));
        } else if ("spec".equals(source)) {
            for (String account : def.getAccount().split(",")) {
                IUser user = this.UserService.getUserByAccount(account);
                if (user == null) {
                    throw new BusinessException(account + "用户丢失", BpmStatusCode.USER_CALC_ERROR);
                }
                list.add(new BpmIdentity(user));
            }
        }
        return list;
    }
}
