package org.openbpm.sys.core.model.def;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * <pre>
 * 描述：SysDataSourceDef的属性字段，SysDataSource也直接复用了
 * </pre>
 */
@Data
public class SysDataSourceDefAttribute implements Serializable {
    /**
     * 名字
     */
    @NotEmpty
    private String name;
    /**
     * 描述
     */
    @NotEmpty
    private String comment;
    /**
     * 参数类型
     */
    @NotEmpty
    private String type;
    /**
     * 是否必填
     */
    private boolean required;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 值，这个字段
     */
    private String value;
    

}
