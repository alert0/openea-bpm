package org.openbpm.base.core.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.openbpm.base.api.model.IBaseModel;
import org.openbpm.base.core.util.ToStringUtil;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

/**
 * <pre>
 * 描述：框架所有Model都需要继承的model
 * 里面有一些系统常用字段，当不需要持久化时map不要配置这些字段则可
 * version字段会在manager.update时自动+1 切记别重复加1了
 * </pre>
 */
@Data
public abstract class BaseModel extends ToStringUtil implements IBaseModel {
    // 主键
    @NotEmpty
    protected String id;
    // 创建时间
    protected Date createTime;
    // 创建人ID
    protected String createBy;
    // 更新时间
    protected Date updateTime;
    // 更新人ID
    protected String updateBy;
    // 版本号
    protected int version = 0;
    // 逻辑删除标记
    protected boolean delete = false;


    @Override
    public String toString() {
    	return JSONObject.toJSONString(this);
    }

}
