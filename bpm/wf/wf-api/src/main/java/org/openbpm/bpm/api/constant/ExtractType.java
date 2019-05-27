package org.openbpm.bpm.api.constant;

public enum ExtractType {
    EXACT_NOEXACT("no", "不抽取"),
    EXACT_EXACT_USER("extract", "抽取用户");
    
    private String key;
    private String value;

    private ExtractType(String key2, String value2) {
        this.key = "";
        this.value = "";
        this.key = key2;
        this.value = value2;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key2) {
        this.key = key2;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value2) {
        this.value = value2;
    }

    public String toString() {
        return this.key;
    }

    public static ExtractType fromKey(String key2) {
        ExtractType[] values;
        for (ExtractType c : values()) {
            if (c.getKey().equalsIgnoreCase(key2)) {
                return c;
            }
        }
        throw new IllegalArgumentException(key2);
    }
}
