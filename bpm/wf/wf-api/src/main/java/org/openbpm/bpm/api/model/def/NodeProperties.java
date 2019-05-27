package org.openbpm.bpm.api.model.def;

import java.io.Serializable;

public class NodeProperties implements Serializable {
    public static final String BACK_MODEL_BACK = "back";
    public static final String BACK_MODEL_NORMAL = "normal";
    public static final String BACK_USER_MODEL_HISTORY = "history";
    public static final String BACK_USER_MODEL_NORMAL = "normal";
    private static final long serialVersionUID = -3157546646728816168L;
    private boolean allowExecutorEmpty = true;
    private String backMode = "normal";
    private String backNode = "";
    private String backUserMode = BACK_USER_MODEL_HISTORY;
    private boolean freeSelectNode = false;
    private String freeSelectUser = "no";
    private String jumpType = "";
    private String nodeId = "";

    public String getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(String nodeId2) {
        this.nodeId = nodeId2;
    }

    public String getJumpType() {
        return this.jumpType;
    }

    public void setJumpType(String jumpType2) {
        this.jumpType = jumpType2;
    }

    public boolean isAllowExecutorEmpty() {
        return this.allowExecutorEmpty;
    }

    public void setAllowExecutorEmpty(boolean allowExecutorEmpty2) {
        this.allowExecutorEmpty = allowExecutorEmpty2;
    }

    public String getBackMode() {
        return this.backMode;
    }

    public void setBackMode(String backMode2) {
        this.backMode = backMode2;
    }

    public String getBackNode() {
        return this.backNode;
    }

    public void setBackNode(String backNode2) {
        this.backNode = backNode2;
    }

    public String getBackUserMode() {
        return this.backUserMode;
    }

    public boolean isFreeSelectNode() {
        return this.freeSelectNode;
    }

    public void setFreeSelectNode(boolean freeSelectNode2) {
        this.freeSelectNode = freeSelectNode2;
    }

    public void setBackUserMode(String backUserMode2) {
        this.backUserMode = backUserMode2;
    }

    public String getFreeSelectUser() {
        return this.freeSelectUser;
    }

    public void setFreeSelectUser(String freeSelectUser2) {
        this.freeSelectUser = freeSelectUser2;
    }
}
