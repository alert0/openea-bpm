package org.openbpm.bpm.act.util;

import cn.hutool.core.util.ObjectUtil;
import org.openbpm.base.core.util.AppUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

public class ActivitiUtil {
    public static CommandExecutor getCommandExecutor() {
        return ((ProcessEngineImpl) AppUtil.getBean(ProcessEngine.class)).getProcessEngineConfiguration().getCommandExecutor();
    }

    public static Map<String, Object> getOutTrans(String actDefId, String nodeId, String[] aryDestination) {
        Map<String, Object> map = new HashMap<>();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) ((RepositoryService) AppUtil.getBean(RepositoryService.class))).getDeployedProcessDefinition(actDefId);
        ActivityImpl curAct = processDefinition.findActivity(nodeId);
        List<PvmTransition> outTrans = curAct.getOutgoingTransitions();
        try {
            map.put("outTrans", (List) ObjectUtil.cloneByStream(outTrans));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        for (PvmTransition transition : outTrans) {
            Iterator<PvmTransition> itIn = transition.getDestination().getIncomingTransitions().iterator();
            while (itIn.hasNext()) {
                if (((PvmTransition) itIn.next()).getSource().getId().equals(curAct.getId())) {
                    itIn.remove();
                }
            }
        }
        curAct.getOutgoingTransitions().clear();
        if (aryDestination != null && aryDestination.length > 0) {
            int length = aryDestination.length;
            for (int i = 0; i < length; i++) {
                curAct.createOutgoingTransition().setDestination(processDefinition.findActivity(aryDestination[i]));
            }
        }
        map.put("activity", curAct);
        return map;
    }

    public static void updateOutTrans(Map<String, Object> map) {
        ActivityImpl curAct = (ActivityImpl) map.get("activity");
        List<PvmTransition> outTrans = (List) map.get("outTrans");
        curAct.getOutgoingTransitions().clear();
        curAct.getOutgoingTransitions().addAll(outTrans);
    }
}
