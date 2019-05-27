package org.openbpm.bpm.api.engine.action.cmd;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.bpm.api.model.inst.IBpmInstance;
import org.openbpm.bus.api.model.IBusinessData;
import org.openbpm.sys.api.model.SysIdentity;
import java.util.List;
import java.util.Map;

public interface ActionCmd {
    public static final String DATA_MODE_BO = "bo";
    public static final String DATA_MODE_PK = "pk";

    void addVariable(String str, Object obj);

    String executeCmd();

    String getActionName();

    Map<String, Object> getActionVariables();

    Map<String, IBusinessData> getBizDataMap();

    Map<String, List<SysIdentity>> getBpmIdentities();

    List<SysIdentity> getBpmIdentity(String str);

    IBpmInstance getBpmInstance();

    JSONObject getBusData();

    String getBusinessKey();

    String getDataMode();

    String getDefId();

    String getDestination();

    String getFormId();

    Object getVariable(String str);

    Map<String, Object> getVariables();

    boolean hasVariable(String str);

    void removeVariable(String str);

    void setActionName(String str);

    void setActionVariables(Map<String, Object> map);

    void setBpmIdentity(String str, List<SysIdentity> list);

    void setBusData(JSONObject jSONObject);

    void setBusinessKey(String str);

    void setDataMode(String str);
}
