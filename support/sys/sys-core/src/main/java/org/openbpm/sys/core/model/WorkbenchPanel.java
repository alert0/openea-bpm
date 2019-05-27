package org.openbpm.sys.core.model;

import lombok.Data;
import org.openbpm.base.core.model.BaseModel;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 */

@Data
public class WorkbenchPanel extends BaseModel {
    public static final String DATA_TYPE_INTERFACE = "interface";
    public static final String DATA_TYPE_CUST_QUERY = "custQuery";


    /**
     * 标识
     */
    protected String alias;

    /**
     * 名字
     */
    protected String name;

    /**
     * 类型，不同类型的panel有不同的指令来实现
     */
    protected String type;

    /**
     * 描述
     */
    protected String desc;

    /**
     * 数据类型
     */
    protected String dataType;

    /**
     * 数据来源
     */
    protected String dataSource;

    /**
     * 自动刷新
     */
    protected Integer autoRefresh;

    /**
     * 宽
     */
    protected Integer width;

    /**
     * 高
     */
    protected Integer height;


    /**
     * 展示内容
     */
    protected String displayContent;

    /**
     * 更多链接
     */
    protected String moreUrl;

    /**
     * 所属系统
     */
    protected String system;

    /**
     * create_time_
     */
    protected java.util.Date createTime;

    /**
     * create_by_
     */
    protected String createBy;

    /**
     * update_time_
     */
    protected java.util.Date updateTime;

    /**
     * update_by_
     */
    protected String updateBy;

    /**
     * delete_flag_
     */
    protected String deleteFlag;
    /**
     * 自定义宽
     */
    protected Integer custWidth;
    /**
     * 自定义高
     */
    protected Integer custHeight;

    /**
     * 自定义布局ID
     */
    protected String custLayOutId;




    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("alias", this.alias)
                .append("name", this.name)
                .append("type", this.type)
                .append("desc", this.desc)
                .append("dataType", this.dataType)
                .append("dataSource", this.dataSource)
                .append("autoRefresh", this.autoRefresh)
                .append("width", this.width)
                .append("height", this.height)
                .append("displayContent", this.displayContent)
                .append("moreUrl", this.moreUrl)
                .append("createTime", this.createTime)
                .append("createBy", this.createBy)
                .append("updateTime", this.updateTime)
                .append("updateBy", this.updateBy)
                .append("deleteFlag", this.deleteFlag)
                .toString();
    }
}