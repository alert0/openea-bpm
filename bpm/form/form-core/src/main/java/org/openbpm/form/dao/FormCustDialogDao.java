package org.openbpm.form.dao;

import org.mybatis.spring.annotation.MapperScan;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.form.model.FormCustDialog;

/**
 * form_cust_dialog DAO接口
 *
 */
@MapperScan
public interface FormCustDialogDao extends BaseDao<String, FormCustDialog> {

}
