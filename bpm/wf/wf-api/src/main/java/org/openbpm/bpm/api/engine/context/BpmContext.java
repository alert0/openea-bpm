package org.openbpm.bpm.api.engine.context;

import org.openbpm.bpm.api.engine.action.cmd.ActionCmd;
import org.openbpm.bpm.api.model.def.BpmProcessDef;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class BpmContext {
    private static ThreadLocal<Stack<ActionCmd>> threadActionModel = new ThreadLocal<>();
    private static ThreadLocal<Map<String, BpmProcessDef>> threadBpmProcessDef = new ThreadLocal<>();

    public static void setActionModel(ActionCmd actionModel) {
        getStack(threadActionModel).push(actionModel);
    }

    public static ActionCmd getActionModel() {
        Stack<ActionCmd> stack = getStack(threadActionModel);
        if (stack.isEmpty()) {
            return null;
        }
        return (ActionCmd) stack.peek();
    }

    public static ActionCmd submitActionModel() {
        Stack<ActionCmd> stack = getStack(threadActionModel);
        if (stack.isEmpty()) {
            return null;
        }
        return (ActionCmd) stack.firstElement();
    }

    public static void removeActionModel() {
        Stack stack = getStack(threadActionModel);
        if (!stack.isEmpty()) {
            stack.pop();
        }
    }

    public static BpmProcessDef getProcessDef(String defId) {
        return (BpmProcessDef) getThreadMap(threadBpmProcessDef).get(defId);
    }

    public static void addProcessDef(String defId, BpmProcessDef processDef) {
        getThreadMap(threadBpmProcessDef).put(defId, processDef);
    }

    public static void cleanTread() {
        threadActionModel.remove();
        threadBpmProcessDef.remove();
    }

    protected static <T> Stack<T> getStack(ThreadLocal<Stack<T>> threadLocal) {
        Stack<T> stack = (Stack) threadLocal.get();
        if (stack != null) {
            return stack;
        }
        Stack<T> stack2 = new Stack<>();
        threadLocal.set(stack2);
        return stack2;
    }

    protected static <T> Map<String, T> getThreadMap(ThreadLocal<Map<String, T>> threadMap) {
        Map<String, T> processDefMap = (Map) threadMap.get();
        if (processDefMap != null) {
            return processDefMap;
        }
        Map<String, T> processDefMap2 = new HashMap<>();
        threadMap.set(processDefMap2);
        return processDefMap2;
    }
}
