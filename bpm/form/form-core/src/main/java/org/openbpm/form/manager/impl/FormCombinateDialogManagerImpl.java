package org.openbpm.form.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.api.query.QueryOP;
import org.openbpm.base.db.model.query.DefaultQueryFilter;
import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.form.dao.FormCombinateDialogDao;
import org.openbpm.form.manager.FormCombinateDialogManager;
import org.openbpm.form.model.FormCombinateDialog;

/**
 * <pre>
 * 描述：combinate_dialog 处理实现类
 * </pre>
 */
@Service("combinateDialogManager")
public class FormCombinateDialogManagerImpl extends BaseManager<String, FormCombinateDialog> implements FormCombinateDialogManager {
    @Resource
    FormCombinateDialogDao combinateDialogDao;

    @Override
    public FormCombinateDialog getByAlias(String alias) {
        QueryFilter queryFilter = new DefaultQueryFilter();
        queryFilter.addFilter("alias_", alias, QueryOP.EQUAL);
        List<FormCombinateDialog> combinateDialogs = query(queryFilter);
        if (combinateDialogs == null || combinateDialogs.isEmpty())
            return null;
        return combinateDialogs.get(0);
    }
}
