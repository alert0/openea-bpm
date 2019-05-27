package org.openbpm.org.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import org.openbpm.base.api.model.IDModel;
import org.openbpm.base.api.model.Tree;
import org.openbpm.org.api.model.system.ISysResource;


/**
 * 系统资源
 *
 */
@Data
public class SysResource implements Tree,ISysResource, IDModel {
    /**
     * 主键
     */
    protected String id;

    /**
     * 父资源ID
     */
    protected String parentId;
    
    /**
     * 子系统ID
     */
    protected String systemId;

    /**
     * 资源别名
     */
    protected String alias;

    /**
     * 资源名
     */
    protected String name;

    /**
     * 默认地址
     */
    protected String url;

    /**
     * 显示到菜单(1,显示,0 ,不显示)
     */
    protected Integer enable;
 
    /**
     * OPENED_
     */
    protected Integer opened;
    
    /**
     * 类型 menu，button，link 
     */
    protected String type;

    /**
     * 图标
     */
    protected String icon = "";

    /**
     * 排序
     */
    protected Integer sn;

    /**
     * 创建时间。
     */
    protected Date createTime;
 
    
    protected List<SysResource> children = new ArrayList<SysResource>();

    /**
     * 是否已分配给角色
     */
    protected boolean checked = false;




    public List getChildren() {
        return children;
    }

	@Override
	public void setChildren(List list) {
		this.children = list;
	}

	@Override
	public String getKey() {
		return alias;
	}

}