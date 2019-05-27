package org.openbpm.sys.core.dao;

import org.mybatis.spring.annotation.MapperScan;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.sys.core.model.SysTree;

/**
 * 系统树 DAO接口
 *
 */
@MapperScan
public interface SysTreeDao extends BaseDao<String, SysTree> {

}
