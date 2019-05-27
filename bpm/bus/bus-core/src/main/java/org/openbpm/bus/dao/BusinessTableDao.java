package org.openbpm.bus.dao;

import org.mybatis.spring.annotation.MapperScan;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.bus.model.BusinessTable;

/**
 * <pre>
 * 描述：业务表 DAO接口
 * </pre>
 */
@MapperScan
public interface BusinessTableDao extends BaseDao<String, BusinessTable> {
}
