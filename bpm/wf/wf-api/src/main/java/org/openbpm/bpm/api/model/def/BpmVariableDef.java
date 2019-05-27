package org.openbpm.bpm.api.model.def;

import java.io.Serializable;

public interface BpmVariableDef extends Serializable {
    public static final String VAR_TYPE_BYTES = "bytes";
    public static final String VAR_TYPE_DATE = "date";
    public static final String VAR_TYPE_DOUBLE = "double";
    public static final String VAR_TYPE_FLOAT = "float";
    public static final String VAR_TYPE_INT = "int";
    public static final String VAR_TYPE_JSON = "json";
    public static final String VAR_TYPE_LONG = "long";
    public static final String VAR_TYPE_STRING = "string";
    public static final String VAR_TYPE_XML = "xml";

    String getDataType();

    Object getDefaultVal();

    String getDescription();

    boolean getIsRequired();

    String getKey();

    String getName();

    String getNodeId();

    boolean isRequired();

    void setDataType(String str);

    void setDefaultVal(Object obj);

    void setDescription(String str);

    void setIsRequired(boolean z);

    void setKey(String str);

    void setName(String str);

    void setNodeId(String str);

    void setRequired(boolean z);
}
