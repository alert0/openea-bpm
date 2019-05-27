package org.openbpm.bus.dao;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.bus.model.BusinessPermission;

/**
 * bo权限 DAO接口
 * 
 */
@MapperScan
public interface BusinessPermissionDao extends BaseDao<String, BusinessPermission> {
	BusinessPermission getByObjTypeAndObjVal(@Param("objType") String objType, @Param("objVal") String objVal);
}
