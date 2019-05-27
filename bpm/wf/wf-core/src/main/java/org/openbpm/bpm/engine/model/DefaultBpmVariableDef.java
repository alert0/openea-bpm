package org.openbpm.bpm.engine.model;

import cn.hutool.core.date.DateUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.model.def.BpmVariableDef;

public class DefaultBpmVariableDef implements BpmVariableDef {
    private String dataType = "string";
    private Object defaultVal = "";
    private String description = "";
    private boolean isRequired = false;
    private String key = "";
    private String name = "";
    private String nodeId = "";

    public static Object getValue(String dataType2, String value) {
        if ("double".equals(dataType2)) {
            return new Double(value);
        }
        if ("float".equals(dataType2)) {
            return new Float(value);
        }
        if ("int".equals(dataType2)) {
            if (value == null || StringUtil.isEmpty(value)) {
                return Integer.valueOf(0);
            }
            return new Integer(value);
        } else if ("date".equals(dataType2)) {
            return DateUtil.parse(value);
        } else {
            return value;
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String varKey) {
        this.key = varKey;
    }

    public String getDataType() {
        return this.dataType;
    }

    public void setDataType(String dataType2) {
        this.dataType = dataType2;
    }

    public Object getDefaultVal() {
        return this.defaultVal;
    }

    public void setDefaultVal(Object defaultVal2) {
        this.defaultVal = defaultVal2;
    }

    public void setDefaultVal(String defaulVal2) {
        this.defaultVal = getValue(this.dataType, defaulVal2);
    }

    public boolean getIsRequired() {
        return this.isRequired;
    }

    public void setIsRequired(boolean isRequired2) {
        this.isRequired = isRequired2;
    }

    public boolean isRequired() {
        return this.isRequired;
    }

    public void setRequired(boolean isRequired2) {
        this.isRequired = isRequired2;
    }

    public String getDescription() {
        return this.description == null ? "" : this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(String nodeId2) {
        this.nodeId = nodeId2;
    }
}
