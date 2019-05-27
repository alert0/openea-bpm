package org.openbpm.bpm.engine.model;

import lombok.Data;
import org.openbpm.bpm.core.model.TaskIdentityLink;
import org.openbpm.org.api.model.IUser;
import org.openbpm.sys.api.model.SysIdentity;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class BpmIdentity implements SysIdentity {
    private static final long serialVersionUID = 4416404339210896051L;
    private String id;
    private String name;
    private String type;

    public BpmIdentity() {
    }

    public BpmIdentity(String id2, String name2, String type2) {
        this.id = id2;
        this.name = name2;
        this.type = type2;
    }

    public BpmIdentity(IUser user) {
        this.id = user.getUserId();
        this.name = user.getFullname();
        this.type = TaskIdentityLink.RIGHT_TYPE_USER;
    }

    public int hashCode() {
        return this.id.hashCode() + this.type.hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BpmIdentity)) {
            return false;
        }
        BpmIdentity identity = (BpmIdentity) obj;
        if (!identity.type.equals(this.type) || !identity.id.equals(this.id)) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        BpmIdentity id1 = new BpmIdentity();
        id1.setId("1");
        id1.setType(TaskIdentityLink.RIGHT_TYPE_USER);
        BpmIdentity id2 = new BpmIdentity();
        id2.setId("1");
        id2.setType(TaskIdentityLink.RIGHT_TYPE_USER);
        Set<BpmIdentity> list = new LinkedHashSet<>();
        list.add(id1);
        list.add(id2);
        list.remove(id2);
        System.out.println(list.size());
    }
}
