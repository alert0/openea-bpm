package org.openbpm.bpm.test;

import org.openbpm.bpm.api.engine.action.cmd.ActionCmd;
import org.springframework.stereotype.Component;

@Component
public class TestUrlService {
    public void testSave(ActionCmd cmd) {
        System.err.println(cmd.getBusData().toJSONString());
        cmd.setBusinessKey("123");
    }
}
