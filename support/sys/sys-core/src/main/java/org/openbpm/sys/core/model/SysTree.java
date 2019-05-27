package org.openbpm.sys.core.model;

import lombok.Data;
import org.openbpm.base.core.model.BaseModel;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * <pre>
 * 描述：系统树
 * </pre>
 */

@Data
public class SysTree extends BaseModel {
    /**
     * 别名
     */
    @NotEmpty
    private String key;
    /**
     * 名字
     */
    @NotEmpty
    private String name;
    /**
     * 描述
     */
    private String desc;
    /**
     * 是否系统内置树
     */
    private boolean system;

    // 以下字段与数据库无关
    /**
     * 树的顶部节点
     */
    private List<SysTreeNode> nodes;

}
