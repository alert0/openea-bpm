package org.openbpm.bpm.api.constant;

public enum MultiInstanceType {
    NO("no", "单实例"),
    PARALLEL(BpmConstants.MULTI_INSTANCE_PARALLEL, "并行"),
    SEQUENTIAL("sequential", "串行");
    
    private String key;
    private String value;

    private MultiInstanceType(String key2, String value2) {
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

    public static MultiInstanceType fromKey(String key2) {
        MultiInstanceType[] values;
        for (MultiInstanceType c : values()) {
            if (c.getKey().equalsIgnoreCase(key2)) {
                return c;
            }
        }
        throw new IllegalArgumentException(key2);
    }
}
