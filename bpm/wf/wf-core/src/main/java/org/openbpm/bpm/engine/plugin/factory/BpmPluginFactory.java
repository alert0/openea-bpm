package org.openbpm.bpm.engine.plugin.factory;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.bpm.api.constant.EventType;
import org.openbpm.bpm.api.engine.plugin.context.BpmPluginContext;
import org.openbpm.bpm.engine.plugin.runtime.BpmExecutionPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BpmPluginFactory {
    protected static Logger LOG = LoggerFactory.getLogger(BpmPluginFactory.class);

    public static BpmExecutionPlugin buildExecutionPlugin(BpmPluginContext bpmPluginContext, EventType eventType) {
        if (CollectionUtil.isEmpty(bpmPluginContext.getEventTypes())) {
            return null;
        }
        if (bpmPluginContext.getEventTypes().contains(eventType)) {
            try {
                return (BpmExecutionPlugin) AppUtil.getBean(bpmPluginContext.getPluginClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LOG.trace("插件【{}】未注册[{}]事件，跳过执行！ 插件时机：{}", new Object[]{bpmPluginContext.getTitle(), eventType, bpmPluginContext.getEventTypes()});
            return null;
        }
        return null;
    }
}
