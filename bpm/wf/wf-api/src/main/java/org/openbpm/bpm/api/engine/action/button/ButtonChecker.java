package org.openbpm.bpm.api.engine.action.button;

import org.openbpm.bpm.api.model.nodedef.Button;
import java.util.List;

public interface ButtonChecker {
    boolean isSupport(Button button);

    void specialBtnHandler(List<Button> list);
}
