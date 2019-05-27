package org.openbpm.bpm.api.engine.plugin.runtime;

public interface RunTimePlugin<S, M, R> {
    R execute(S s, M m);
}
