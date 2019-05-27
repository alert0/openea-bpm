package org.openbpm.bpm.api.service;

import org.openbpm.bpm.api.engine.data.result.BpmFlowData;
import org.openbpm.bus.api.model.IBusinessPermission;
import org.openbpm.form.api.model.FormType;
import java.util.Set;

public interface BpmRightsFormService {
    IBusinessPermission getInstanceFormPermission(BpmFlowData bpmFlowData, String str, FormType formType, boolean z);

    IBusinessPermission getNodeSavePermission(String str, String str2, Set<String> set);
}
