package org.openbpm.sys.core.model;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.openbpm.base.api.model.IDModel;

@Data
public class SerialNo implements IDModel{
    protected String id; /*主键*/
    protected String name; /*名称*/
    protected String alias; /*别名*/
    protected String regulation; /*规则*/
    protected Short genType; /*生成类型*/
    protected Integer noLength; /*流水号长度*/
    protected String curDate; /*当前日期*/
    protected Integer initValue; /*初始值*/
    protected Integer curValue = 0; /*当前值*/
    protected Short step; /*步长*/

    /**
     * 新的流水号。
     */
    protected Integer newCurValue = 0;

    /**
     * 预览流水号。
     */
    protected String curIdenValue = "";



    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("name", this.name)
                .append("alias", this.alias)
                .append("regulation", this.regulation)
                .append("genType", this.genType)
                .append("noLength", this.noLength)
                .append("curDate", this.curDate)
                .append("initValue", this.initValue)
                .append("curValue", this.curValue)
                .append("step", this.step)
                .toString();
    }
}