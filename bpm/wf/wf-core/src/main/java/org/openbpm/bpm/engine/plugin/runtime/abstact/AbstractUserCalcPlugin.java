package org.openbpm.bpm.engine.plugin.runtime.abstact;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.bpm.api.constant.ExtractType;
import org.openbpm.bpm.api.engine.plugin.def.BpmUserCalcPluginDef;
import org.openbpm.bpm.core.model.TaskIdentityLink;
import org.openbpm.bpm.engine.model.BpmIdentity;
import org.openbpm.bpm.engine.plugin.plugindef.AbstractUserCalcPluginDef;
import org.openbpm.bpm.engine.plugin.runtime.BpmUserCalcPlugin;
import org.openbpm.bpm.engine.plugin.session.BpmUserCalcPluginSession;
import org.openbpm.org.api.model.IUser;
import org.openbpm.org.api.service.UserService;
import org.openbpm.sys.api.model.SysIdentity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;

public abstract class AbstractUserCalcPlugin<M extends BpmUserCalcPluginDef> implements BpmUserCalcPlugin<M> {
    @Resource
    protected UserService userService;

    protected abstract List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession bpmUserCalcPluginSession, M m);

    public List<SysIdentity> execute(BpmUserCalcPluginSession pluginSession, M pluginDef) {
        if (pluginSession.isPreVrewModel().booleanValue() && !supportPreView()) {
            return Collections.emptyList();
        }
        List<SysIdentity> list = queryByPluginDef(pluginSession, pluginDef);
        if (CollectionUtil.isEmpty(list)) {
            return list;
        }
        ExtractType extractType = ((AbstractUserCalcPluginDef) pluginDef).getExtract();
        Set<SysIdentity> set = new LinkedHashSet<>();
        List<SysIdentity> arrayList = new ArrayList<>();
        set.addAll(extract(list, extractType));
        arrayList.addAll(set);
        return arrayList;
    }

    protected List<SysIdentity> extract(List<SysIdentity> bpmIdentities, ExtractType extractType) {
        if (CollectionUtil.isEmpty(bpmIdentities)) {
            return Collections.EMPTY_LIST;
        }
        return extractType != ExtractType.EXACT_NOEXACT ? extractBpmIdentity(bpmIdentities) : bpmIdentities;
    }

    protected List<SysIdentity> extractBpmIdentity(List<SysIdentity> bpmIdentities) {
        List<SysIdentity> results = new ArrayList<>();
        for (SysIdentity bpmIdentity : bpmIdentities) {
            if (TaskIdentityLink.RIGHT_TYPE_USER.equals(bpmIdentity.getType())) {
                results.add(bpmIdentity);
            } else {
                for (IUser user : this.userService.getUserListByGroup(bpmIdentity.getType(), bpmIdentity.getId())) {
                    results.add(new BpmIdentity(user));
                }
            }
        }
        return results;
    }

    public boolean supportPreView() {
        return true;
    }
}
