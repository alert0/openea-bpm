package org.openbpm.sys.core.model;

import lombok.Data;
import org.openbpm.base.core.model.BaseModel;


/**
 */

@Data
public class WorkbenchLayout extends BaseModel {
    public static final String DEFAULT_LAYOUT = "default_layout";

    /**
     * id_
     */
    protected String id;

    /**
     * 面板id
     */
    protected String panelId;

    /**
     * 自定义宽
     */
    protected Integer custWidth;

    /**
     * 自定义高
     */
    protected Integer custHeight;

    /**
     * 排序
     */
    protected Integer sn;

    /**
     * 用户id
     */
    protected String userId;

    /**
     * 创建时间
     */
    protected java.util.Date createTime;


}