package org.openbpm.sys.core.model;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.openbpm.base.core.model.BaseModel;

@Data
public class Script extends BaseModel implements Cloneable {

    private static final long serialVersionUID = 1L;
    protected String name;        /*名称*/
    protected String script;    /*脚本*/
    protected String category;    /*脚本分类*/
    protected String memo;        /*备注*/


    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("name", this.name)
                .append("script", this.script)
                .append("category", this.category)
                .append("memo", this.memo)
                .toString();
    }
}
