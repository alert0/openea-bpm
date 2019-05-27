package org.openbpm.sys.core.dao;

import org.mybatis.spring.annotation.MapperScan;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.sys.core.model.SysDataSource;

/**
 * <pre>
 * 描述：数据源 DAO接口
 * </pre>
 */
@MapperScan
public interface SysDataSourceDao extends BaseDao<String, SysDataSource> {
}
