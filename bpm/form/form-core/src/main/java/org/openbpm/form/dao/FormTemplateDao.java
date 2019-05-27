package org.openbpm.form.dao;

import org.mybatis.spring.annotation.MapperScan;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.form.model.FormTemplate;
@MapperScan
public interface FormTemplateDao extends BaseDao<String, FormTemplate> {
}
