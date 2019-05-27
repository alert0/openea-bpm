package org.openbpm.bpm.api.engine.service;

import org.openbpm.org.api.service.UserService;

public class BpmEngineService {
    private UserService userService;

    public UserService getUserService() {
        return this.userService;
    }

    public void setUserService(UserService userService2) {
        this.userService = userService2;
    }
}
