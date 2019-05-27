package org.openbpm.sys.core.manager;

import org.openbpm.base.manager.Manager;
import org.openbpm.sys.core.model.SysDataSourceDef;
import org.openbpm.sys.core.model.def.SysDataSourceDefAttribute;

import java.util.List;

/**
 * <pre>
 * 描述：数据源模板 Manager处理接口
 * 构建组：白日梦团体
 * </pre>
 */
public interface SysDataSourceDefManager extends Manager<String, SysDataSourceDef> {

    /**
     * <pre>
     * 根据classPath类路径获取数据源的配置参数
     * </pre>
     *
     * @param classPath
     * @return
     */
    List<SysDataSourceDefAttribute> initAttributes(String classPath);

}
