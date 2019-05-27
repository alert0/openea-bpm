package org.openbpm.bpm.plugin.usercalc.user.def;

public class ExecutorVar {
    public static final String EXECUTOR_TYPE_GROUP = "group";
    public static final String EXECUTOR_TYPE_USER = "user";
    public static final String SOURCE_BO = "BO";
    public static final String SOURCE_FLOW_VAR = "flowVar";
    private String dimension = "";
    private String executorType = "";
    private String groupValType = "";
    private String name = "";
    private String source = "";
    private String userValType = "";
    private String value;

    public ExecutorVar() {
    }

    public ExecutorVar(String source2, String name2, String executorType2, String userValType2, String groupValType2, String dimension2) {
        this.source = source2;
        this.name = name2;
        this.executorType = executorType2;
        this.userValType = userValType2;
        this.groupValType = groupValType2;
        this.dimension = dimension2;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source2) {
        this.source = source2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getExecutorType() {
        return this.executorType;
    }

    public void setExecutorType(String executorType2) {
        this.executorType = executorType2;
    }

    public String getUserValType() {
        return this.userValType;
    }

    public void setUserValType(String userValType2) {
        this.userValType = userValType2;
    }

    public String getGroupValType() {
        return this.groupValType;
    }

    public void setGroupValType(String groupValType2) {
        this.groupValType = groupValType2;
    }

    public String getDimension() {
        return this.dimension;
    }

    public void setDimension(String dimension2) {
        this.dimension = dimension2;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value2) {
        this.value = value2;
    }
}
