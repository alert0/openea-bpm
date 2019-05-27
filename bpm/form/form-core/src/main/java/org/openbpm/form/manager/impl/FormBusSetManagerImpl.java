package org.openbpm.form.manager.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.form.dao.FormBusSetDao;
import org.openbpm.form.manager.FormBusSetManager;
import org.openbpm.form.model.FormBusSet;

/**
 * <pre>
 * 描述：form_bus_set 处理实现类
 * </pre>
 */
@Service("formBusSetManager")
public class FormBusSetManagerImpl extends BaseManager<String, FormBusSet> implements FormBusSetManager {
    @Resource
    FormBusSetDao formBusSetDao;

    @Override
    public FormBusSet getByFormKey(String formKey) {
        return formBusSetDao.getByFormKey(formKey);
    }
}
