package org.openbpm.bpm.engine.parser.node;

import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.api.model.nodedef.impl.BaseBpmNodeDef;
import org.openbpm.bpm.engine.parser.BaseBpmDefParser;

public abstract class AbsNodeParse<T> extends BaseBpmDefParser<T, BaseBpmNodeDef> {
    public boolean isSupport(BpmNodeDef nodeDef) {
        return true;
    }
}
