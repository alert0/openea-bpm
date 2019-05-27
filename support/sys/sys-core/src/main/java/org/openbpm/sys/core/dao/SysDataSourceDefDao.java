package org.openbpm.sys.core.dao;

import org.mybatis.spring.annotation.MapperScan;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.sys.core.model.SysDataSourceDef;

/**
 * <pre>
 * 描述：数据源模板 DAO接口
 * 构建组：白日梦团体
 * </pre>
 */
@MapperScan
public interface SysDataSourceDefDao extends BaseDao<String, SysDataSourceDef> {
}
