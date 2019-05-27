package org.openbpm.bpm.act.service;

import java.util.Map;
import org.activiti.engine.delegate.DelegateTask;

public interface ActTaskService {
    void completeTask(String str);

    void completeTask(String str, Map<String, Object> map);

    void completeTask(String str, Map<String, Object> map, String... strArr);

    void completeTask(String str, String... strArr);

    DelegateTask getByTaskId(String str);

    Object getVariable(String str, String str2);

    Map<String, Object> getVariables(String str);

    void setVariable(String str, String str2, Object obj);

    void setVariables(String str, Map<String, ? extends Object> map);
}
