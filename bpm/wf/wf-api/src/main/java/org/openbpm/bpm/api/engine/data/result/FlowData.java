package org.openbpm.bpm.api.engine.data.result;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.bpm.api.model.form.BpmForm;
import org.openbpm.bpm.api.model.nodedef.Button;
import java.util.List;

public interface FlowData {
    List<Button> getButtonList();

    JSONObject getData();

    String getDefId();

    BpmForm getForm();

    JSONObject getInitData();

    JSONObject getPermission();

    JSONObject getTablePermission();
}
