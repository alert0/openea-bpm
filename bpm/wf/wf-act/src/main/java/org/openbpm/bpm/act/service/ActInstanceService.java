package org.openbpm.bpm.act.service;

import java.util.Collection;
import java.util.Map;
import org.activiti.engine.runtime.ProcessInstance;

public interface ActInstanceService {
    void deleteProcessInstance(String str, String str2);

    void endProcessInstance(String str);

    ProcessInstance getProcessInstance(String str);

    Object getSuperVariable(String str, String str2);

    Object getVariable(String str, String str2);

    Map<String, Object> getVariables(String str);

    Map<String, Object> getVariables(String str, Collection<String> collection);

    boolean hasVariable(String str, String str2);

    void removeVariable(String str, String str2);

    void removeVariables(String str, Collection<String> collection);

    void setVariable(String str, String str2, Object obj);

    void setVariables(String str, Map<String, ? extends Object> map);

    String startProcessInstance(String str, String str2, Map<String, Object> map);

    String startProcessInstance(String str, String str2, Map<String, Object> map, String... strArr);
}
