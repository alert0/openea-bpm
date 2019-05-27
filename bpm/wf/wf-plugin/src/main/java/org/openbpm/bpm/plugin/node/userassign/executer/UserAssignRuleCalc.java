package org.openbpm.bpm.plugin.node.userassign.executer;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.constant.ExtractType;
import org.openbpm.bpm.api.engine.constant.LogicType;
import org.openbpm.bpm.api.engine.plugin.context.UserCalcPluginContext;
import org.openbpm.bpm.api.engine.plugin.def.BpmUserCalcPluginDef;
import org.openbpm.bpm.api.engine.plugin.def.UserAssignRule;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.exception.WorkFlowException;
import org.openbpm.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
import org.openbpm.bpm.engine.plugin.session.BpmUserCalcPluginSession;
import org.openbpm.sys.api.groovy.IGroovyScriptEngine;
import org.openbpm.sys.api.model.SysIdentity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAssignRuleCalc {
    protected static final Logger LOG = LoggerFactory.getLogger(UserAssignRuleCalc.class);

    public static List<SysIdentity> calcUserAssign(BpmUserCalcPluginSession bpmUserCalcPluginSession, List<UserAssignRule> ruleList, Boolean forceExtract) {
        List<SysIdentity> bpmIdentities = new ArrayList<>();
        Collections.sort(ruleList);
        for (UserAssignRule userRule : ruleList) {
            if (bpmIdentities.size() > 0) {
                break;
            } else if (isRuleValid(userRule.getCondition(), bpmUserCalcPluginSession)) {
                for (UserCalcPluginContext context : userRule.getCalcPluginContextList()) {
                    AbstractUserCalcPlugin plugin = (AbstractUserCalcPlugin) AppUtil.getBean(context.getPluginClass());
                    if (plugin == null) {
                        throw new WorkFlowException("请检查该插件是否注入成功：" + context.getPluginClass(), BpmStatusCode.PLUGIN_ERROR);
                    }
                    BpmUserCalcPluginDef pluginDef = (BpmUserCalcPluginDef) context.getBpmPluginDef();
                    if (forceExtract.booleanValue()) {
                        pluginDef.setExtract(ExtractType.EXACT_EXACT_USER);
                    }
                    List<SysIdentity> biList = plugin.execute(bpmUserCalcPluginSession, pluginDef);
                    LOG.debug("执行用户计算插件【{}】，解析到【{}】条人员信息，插件计算逻辑：{}", new Object[]{context.getTitle(), Integer.valueOf(biList.size()), pluginDef.getLogicCal()});
                    if (!CollectionUtil.isEmpty(biList)) {
                        if (CollectionUtil.isEmpty(bpmIdentities)) {
                            bpmIdentities.addAll(biList);
                        } else {
                            calc(bpmIdentities, biList, pluginDef.getLogicCal());
                        }
                    }
                }
                continue;
            }
        }
        return bpmIdentities;
    }

    private static void calc(List<SysIdentity> existBpmIdentities, List<SysIdentity> newBpmIdentities, LogicType logic) {
        switch (logic) {
            case OR:
                Set<SysIdentity> set = new LinkedHashSet<>();
                set.addAll(existBpmIdentities);
                set.addAll(newBpmIdentities);
                existBpmIdentities.clear();
                existBpmIdentities.addAll(set);
                return;
            case AND:
                List<SysIdentity> rtnList = new ArrayList<>();
                for (SysIdentity identity : existBpmIdentities) {
                    for (SysIdentity tmp : newBpmIdentities) {
                        if (identity.equals(tmp)) {
                            rtnList.add(identity);
                        }
                    }
                }
                existBpmIdentities.clear();
                existBpmIdentities.addAll(rtnList);
                return;
            default:
                for (SysIdentity tmp2 : newBpmIdentities) {
                    existBpmIdentities.remove(tmp2);
                }
                return;
        }
    }

    private static boolean isRuleValid(String script, BpmUserCalcPluginSession bpmUserCalcPluginSession) {
        if (StringUtil.isEmpty(script)) {
            return true;
        }
        Map<String, Object> map = new HashMap<>();
        map.putAll(bpmUserCalcPluginSession.getBoDatas());
        map.put("bpmTask", bpmUserCalcPluginSession.getBpmTask());
        map.put("bpmInstance", bpmUserCalcPluginSession.getBpmInstance());
        map.put("variableScope", bpmUserCalcPluginSession.getVariableScope());
        try {
            return ((IGroovyScriptEngine) AppUtil.getBean(IGroovyScriptEngine.class)).executeBoolean(script, map);
        } catch (Exception e) {
            LOG.error("人员前置脚本解析失败,脚本：{},可能原因为：{}", new Object[]{script, e.getMessage(), e});
            throw new BusinessException(BpmStatusCode.PLUGIN_USERCALC_RULE_CONDITION_ERROR);
        }
    }
}
