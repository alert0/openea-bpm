package org.openbpm.bpm.act.listener;

import org.openbpm.base.core.util.AppUtil;
import java.util.HashMap;
import java.util.Map;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.impl.ActivitiEventImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GlobalEventListener implements ActivitiEventListener {
    private Map<String, String> handlers = new HashMap();
    public Log logger = LogFactory.getLog(GlobalEventListener.class);

    public void onEvent(ActivitiEvent event) {
        String eventType = event.getType().name();
        String eventHandlerBeanId = (String) this.handlers.get(eventType);
        if (eventHandlerBeanId != null) {
            ((ActEventListener) AppUtil.getBean(eventHandlerBeanId)).notify((ActivitiEventImpl) event);
        } else {
            this.logger.debug("eventListener:" + eventType + " skiped");
        }
    }

    public boolean isFailOnException() {
        return true;
    }

    public Map<String, String> getHandlers() {
        return this.handlers;
    }

    public void setHandlers(Map<String, String> handlers2) {
        this.handlers = handlers2;
    }
}
