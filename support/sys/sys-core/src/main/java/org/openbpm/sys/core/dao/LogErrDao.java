package org.openbpm.sys.core.dao;

import org.mybatis.spring.annotation.MapperScan;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.sys.core.model.LogErr;

/**
 * <pre>
 * 描述：错误日志 DAO接口
 * </pre>
 */
@MapperScan
public interface LogErrDao extends BaseDao<String, LogErr> {
}
