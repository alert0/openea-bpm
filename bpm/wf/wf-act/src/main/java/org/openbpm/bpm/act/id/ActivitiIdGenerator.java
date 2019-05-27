package org.openbpm.bpm.act.id;

import org.activiti.engine.impl.cfg.IdGenerator;

public class ActivitiIdGenerator implements IdGenerator {
    private org.openbpm.base.core.id.IdGenerator idGenerator = null;

    public void setIdGenerator(org.openbpm.base.core.id.IdGenerator idGenerator2) {
        this.idGenerator = idGenerator2;
    }

    public String getNextId() {
        return this.idGenerator.getSuid();
    }
}
