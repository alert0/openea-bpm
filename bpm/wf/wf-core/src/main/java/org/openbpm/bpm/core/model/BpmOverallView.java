package org.openbpm.bpm.core.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.openbpm.bus.api.model.IBusinessPermission;
import java.util.ArrayList;
import java.util.List;

@Data
public class BpmOverallView {
    public static final String IMPORT_TYPE_EDIT = "edit";
    public static final String IMPORT_TYPE_NEW_VERSION = "newVersion";
    public static final String IMPORT_TYPE_OVERRIDE = "override";


    private transient BpmDefinition bpmDefinition;
    private String bpmnXml;
    private String defId;
    private JSONObject defSetting;
    private List<IBusinessPermission> formRights = new ArrayList();
    private String importType = "edit";
    private Boolean isUpdateVersion;

}
