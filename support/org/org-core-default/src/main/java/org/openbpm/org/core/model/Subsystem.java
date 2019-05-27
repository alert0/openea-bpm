package org.openbpm.org.core.model;

import lombok.Data;
import org.openbpm.base.core.model.BaseModel;
import org.openbpm.org.api.model.system.ISubsystem;

/**
 * 描述：子系统定义 实体对象
 */
@Data
public class Subsystem extends BaseModel implements  ISubsystem {

    /**
     * 主键
     */
    protected String id;

    /**
     * 系统名称
     */
    protected String name;

    /**
     * 系统别名
     */
    protected String alias;

    /**
     * 是否可用 1 可用，0 ，不可用
     */
    protected Integer enabled = 1;

    /**
     * 描述
     */
    protected String desc;

    /**
     * 是否主系统
     */
    protected int isDefault = 0;
    
    protected String config;


}