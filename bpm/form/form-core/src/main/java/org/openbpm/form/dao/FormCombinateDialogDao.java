package org.openbpm.form.dao;

import org.mybatis.spring.annotation.MapperScan;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.form.model.FormCombinateDialog;

/**
 * <pre>
 * 描述：combinate_dialog DAO接口
 * </pre>
 */
@MapperScan
public interface FormCombinateDialogDao extends BaseDao<String, FormCombinateDialog> {
}
