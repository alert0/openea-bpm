package org.openbpm.bpm.api.model.task;

public interface BpmTaskCandidate {
    String getExecutor();

    String getId();

    String getProcInstId();

    String getTaskId();

    String getType();
}
