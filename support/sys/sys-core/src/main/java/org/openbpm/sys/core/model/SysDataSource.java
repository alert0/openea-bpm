package org.openbpm.sys.core.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import org.openbpm.base.core.model.BaseModel;
import org.openbpm.base.core.util.JsonUtil;
import org.openbpm.sys.api.model.ISysDataSource;
import org.openbpm.sys.core.model.def.SysDataSourceDefAttribute;

/**
 * <pre>
 * 描述：数据源对象
 * </pre>
 */

@Data
public class SysDataSource extends BaseModel implements ISysDataSource {
    /**
     * 数据源的别名
     */
    @NotEmpty
    private String key;
    /**
     * 数据源的名字
     */
    @NotEmpty
    private String name;
    /**
     * 描述
     */
    private String desc;
    /**
     * 数据库类型 枚举在org.openbpm.base.api.db.DbType 的key
     */
    @NotEmpty
    private String dbType;
    /**
     * 类路径
     */
    @NotEmpty
    private String classPath;
    /**
     * <pre>
     * 属性字段json，为了简单就以json格式入库就行
     * 因为这个对象也不常用，这样保存是可以的，对于常用对象这样就不建议用这个了
     * </pre>
     */
    @NotEmpty
    private String attributesJson;
    /**
     * 属性字段
     */
    @NotNull
    @Valid
    private List<SysDataSourceDefAttribute> attributes;


    public void setAttributesJson(String attributesJson) {
        this.attributesJson = attributesJson;
        this.attributes = JsonUtil.parseArray(attributesJson, SysDataSourceDefAttribute.class);
    }

    public void setAttributes(List<SysDataSourceDefAttribute> attributes) {
        this.attributes = attributes;
        this.attributesJson = JsonUtil.toJSONString(attributes);
    }

}
