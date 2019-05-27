package org.openbpm.bus.dao;

import org.mybatis.spring.annotation.MapperScan;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.bus.model.BusinessObject;

/**
 * <pre>
 * 描述：BusinessObject Dao层
 * </pre>
 */
@MapperScan
public interface BusinessObjectDao extends BaseDao<String, BusinessObject> {
}
