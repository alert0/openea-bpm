package org.openbpm.form.dao;

import org.mybatis.spring.annotation.MapperScan;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.form.model.FormDef;

/**
 * 表单 DAO接口
 *
 */
@MapperScan
public interface FormDefDao extends BaseDao<String, FormDef> {

	FormDef getByKey(String key);

}
