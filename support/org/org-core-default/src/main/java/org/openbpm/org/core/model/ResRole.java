package org.openbpm.org.core.model;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.openbpm.base.api.model.IDModel;


/**
 * <pre>
 * 描述：角色资源分配 实体对象
 * </pre>
 */
@Data
public class ResRole implements IDModel {

    /**
     * 主键
     */
    protected String id;

    /**
     * 系统ID
     */
    protected String systemId;

    /**
     * 资源ID
     */
    protected String resId;

    /**
     * 角色ID
     */
    protected String roleId;

    /**
     * 角色别名。
     */
    protected String roleAlias;
    /**
     * 资源url连接。
     */
    protected String url;

    /**
     * 资源别名。
     */
    protected String resAlias;



    public ResRole(String systemId, String resId, String roleId) {
		super();
		this.systemId = systemId;
		this.resId = resId;
		this.roleId = roleId;
	}
    
    public ResRole() {
	}


    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("systemId", this.systemId)
                .append("resId", this.resId)
                .append("roleId", this.roleId)
                .toString();
    }
}