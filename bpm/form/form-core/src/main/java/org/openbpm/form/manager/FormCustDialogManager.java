package org.openbpm.form.manager;

import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.db.model.table.Column;
import org.openbpm.base.db.model.table.Table;
import org.openbpm.base.manager.Manager;
import org.openbpm.form.model.FormCustDialog;

import java.util.List;
import java.util.Map;

/**
 * form_cust_dialog Manager处理接口
 *
 */
public interface FormCustDialogManager extends Manager<String, FormCustDialog> {

    /**
     * <pre>
     * 根据key获取FormCustDialog
     * </pre>
     *
     * @param key
     * @return
     */
    FormCustDialog getByKey(String key);

    /**
     * <pre>
     * 查询ObjName
     * </pre>
     *
     * @param formCustDialog
     * @return
     */
    Map<String, String> searchObjName(FormCustDialog formCustDialog);

    /**
     * <pre>
     * 获取objName对象的表 / 视图信息
     * </pre>
     *
     * @param formCustDialog
     * @return
     */
    Table<Column> getTable(FormCustDialog formCustDialog);

    /**
     * <pre>
     * 根据formCustDialog获取数据
     * 包含树形和列表
     * </pre>
     *
     * @param formCustDialog
     * @param queryFilter    页面传参
     * @return
     */
    List<?> data(FormCustDialog formCustDialog, QueryFilter queryFilter);

}
