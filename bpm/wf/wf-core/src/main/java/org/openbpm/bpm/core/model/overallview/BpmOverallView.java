package org.openbpm.bpm.core.model.overallview;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.openbpm.bpm.core.model.BpmDefinition;

@Data
public class BpmOverallView {
    public static final String IMPORT_TYPE_EDIT = "edit";
    public static final String IMPORT_TYPE_NEW_VERSION = "newVersion";
    public static final String IMPORT_TYPE_OVERRIDE = "override";
    private BpmDefinition bpmDefinition;
    private String bpmnXml;
    private String defId;
    private JSONObject defSetting;
    private String importType = "edit";
    private Boolean isUpdateVersion;
    private String modelJson;
    private JSONArray permission = new JSONArray();

}
