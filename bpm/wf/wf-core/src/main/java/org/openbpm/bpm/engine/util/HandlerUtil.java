package org.openbpm.bpm.engine.util;

import org.openbpm.base.core.util.AppUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.engine.action.cmd.ActionCmd;
import java.lang.reflect.Method;

public class HandlerUtil {
    public static int isHandlerValid(String handler) {
        if (handler.indexOf(".") == -1) {
            return -1;
        }
        String[] aryHandler = handler.split("[.]");
        if (aryHandler.length != 2) {
            return -1;
        }
        String beanId = aryHandler[0];
        String method = aryHandler[1];
        try {
            Object serviceBean = AppUtil.getBean(beanId);
            if (serviceBean == null) {
                return -2;
            }
            try {
                Method declaredMethod = serviceBean.getClass().getDeclaredMethod(method, new Class[]{ActionCmd.class});
                return 0;
            } catch (NoSuchMethodException e) {
                return -3;
            } catch (Exception e2) {
                return -4;
            }
        } catch (Exception e3) {
            return -2;
        }
    }

    public static void invokeHandler(ActionCmd actionModel, String handler) throws Exception {
        if (!StringUtil.isEmpty(handler)) {
            String[] aryHandler = handler.split("[.]");
            if (aryHandler != null && aryHandler.length == 2) {
                String beanId = aryHandler[0];
                String method = aryHandler[1];
                Object serviceBean = AppUtil.getBean(beanId);
                if (serviceBean != null) {
                    serviceBean.getClass().getDeclaredMethod(method, new Class[]{ActionCmd.class}).invoke(serviceBean, new Object[]{actionModel});
                }
            }
        }
    }
}
