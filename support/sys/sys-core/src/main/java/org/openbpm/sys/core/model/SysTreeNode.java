package org.openbpm.sys.core.model;

import java.util.List;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import org.openbpm.base.core.model.BaseModel;
import org.openbpm.sys.api.model.ISysTreeNode;

/**
 * <pre>
 * 描述：系统树节点
 * </pre>
 */

@Data
public class SysTreeNode extends BaseModel implements ISysTreeNode {
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
     * 所属树id
     */
    @NotEmpty
    private String treeId;
    /**
     * 父ID
     */
    private String parentId;
    /**
     * 路径 eg:pppid.ppid.pid
     */
    private String path;
    /**
     * 排序号
     */
    private int sn;

    // 以下字段与数据库无关
    /**
     * 当前节点的子节点
     */
    private List<SysTreeNode> children;


}
