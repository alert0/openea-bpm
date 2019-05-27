package org.openbpm.bpm.api.engine.action.button;

import org.openbpm.base.core.util.AppUtil;
import org.openbpm.bpm.api.constant.ActionType;
import org.openbpm.bpm.api.engine.action.handler.ActionHandler;
import org.openbpm.bpm.api.engine.data.result.BpmFlowData;
import org.openbpm.bpm.api.engine.data.result.BpmFlowTaskData;
import org.openbpm.bpm.api.model.def.NodeProperties;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.api.model.nodedef.Button;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ButtonFactory {
    public static List<Button> generateButtons(BpmNodeDef nodeDef, boolean isDefault) throws ClassNotFoundException {
        NodeProperties nodeProperties = nodeDef.getNodeProperties();
        List<Button> btns = new ArrayList<>();
        List<ActionHandler> list = new ArrayList<>(AppUtil.getImplInstance(ActionHandler.class).values());
        sortActionList(list);
        for (ActionHandler actionHandler : list) {
            if ((!isDefault || actionHandler.isDefault().booleanValue()) && actionHandler.isSupport(nodeDef).booleanValue()) {
                ActionType actionType = actionHandler.getActionType();
                Button button = new Button(actionType.getName(), actionType.getKey(), actionHandler.getDefaultGroovyScript(), actionHandler.getConfigPage());
                button.setBeforeScript(actionHandler.getDefaultBeforeScript());
                btns.add(button);
            }
        }
        return btns;
    }

    private static void sortActionList(List<ActionHandler> list) {
        Collections.sort(list, new Comparator<ActionHandler>() {
            public int compare(ActionHandler o1, ActionHandler o2) {
                return o1.getSn() - o2.getSn();
            }
        });
    }

    public static List<Button> getInstanceButtons() {
        List<Button> btns = new ArrayList<>();
        for (ActionHandler actionHandler : new ArrayList<>(AppUtil.getImplInstance(ActionHandler.class).values())) {
            ActionType actionType = actionHandler.getActionType();
            if (ActionType.FLOWIMAGE == actionType || ActionType.PRINT == actionType || ActionType.TASKOPINION == actionType || actionType == ActionType.REMINDER) {
                Button button = new Button(actionType.getName(), actionType.getKey(), actionHandler.getDefaultGroovyScript(), actionHandler.getConfigPage());
                button.setBeforeScript(actionHandler.getDefaultBeforeScript());
                btns.add(button);
            }
        }
        return btns;
    }

    public static List<Button> specialTaskBtnHandler(List<Button> btns, BpmFlowData flowData) {
        if (!(flowData instanceof BpmFlowTaskData)) {
            return btns;
        }
        ButtonChecker checker = (ButtonChecker) AppUtil.getBean(((BpmFlowTaskData) flowData).getTask().getTaskType() + "ButtonChecker");
        if (checker == null) {
            return btns;
        }
        List<Button> arrayList = new ArrayList<>();
        for (Button btn : btns) {
            if (checker.isSupport(btn)) {
                arrayList.add(btn);
            }
        }
        checker.specialBtnHandler(arrayList);
        return arrayList;
    }
}
