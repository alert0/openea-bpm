package org.openbpm.bpm.engine.plugin.util;

import org.openbpm.org.api.model.IUser;
import org.openbpm.sys.api.model.SysIdentity;
import java.util.List;

public class IdentityUtil {
    public static List<IUser> convertUser(List<SysIdentity> identityList) {
        for (SysIdentity sysIdentity : identityList) {
            // TODO
        }
        return null;
    }
}
