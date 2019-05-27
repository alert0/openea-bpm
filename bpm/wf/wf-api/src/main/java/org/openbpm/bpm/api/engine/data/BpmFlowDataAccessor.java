package org.openbpm.bpm.api.engine.data;

import org.openbpm.bpm.api.engine.data.result.BpmFlowData;
import org.openbpm.bpm.api.engine.data.result.BpmFlowInstanceData;
import org.openbpm.bus.api.model.IBusinessData;
import org.openbpm.form.api.model.FormType;
import java.util.Map;

public interface BpmFlowDataAccessor {
    BpmFlowData getFlowTaskData(String str, FormType formType);

    BpmFlowInstanceData getInstanceData(String str, FormType formType, String str2);

    BpmFlowData getStartFlowData(String str, String str2, FormType formType, Boolean bool);

    Map<String, IBusinessData> getTaskBusData(String str);
}
