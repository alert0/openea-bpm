package org.openbpm.org.core.model;

import java.util.Map;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.openbpm.base.core.model.BaseModel;
import org.openbpm.org.api.constant.GroupTypeConstant;
import org.openbpm.org.api.model.IGroup;


/**
 * <pre>
 * 描述：角色管理 实体对象
 * </pre>
 */
@Data
public class Role extends BaseModel implements IGroup {
    /**
     * 角色名称
     */
    protected String name;

    /**
     * 角色别名
     */
    protected String alias;

    /**
     * 0：禁用，1：启用
     */
    protected Integer enabled = 1;

    /**
     * 角色描述
     */
    protected String description = "";



    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("name", this.name)
                .append("alias", this.alias)
                .append("enabled", this.enabled)
                .toString();
    }

    public String getGroupId() {
        return this.id;
    }

    public String getGroupCode() {

        return this.alias;
    }

    public Long getSn() {
        return Long.valueOf(1);
    }

    public String getGroupType() {
        return GroupTypeConstant.ROLE.key();
    }

    public String getParentId() {
        return "";
    }

    public String getPath() {
        return this.name;
    }

    public Map<String, Object> getParams() {

        return null;
    }

	@Override
	public String getGroupName() {
		return this.name;
	}
}