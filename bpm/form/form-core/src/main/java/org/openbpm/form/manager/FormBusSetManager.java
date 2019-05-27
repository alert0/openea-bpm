package org.openbpm.form.manager;

import org.openbpm.base.manager.Manager;
import org.openbpm.form.model.FormBusSet;

/**
 * <pre>
 * 描述：form_bus_set 处理接口
 * </pre>
 */
public interface FormBusSetManager extends Manager<String, FormBusSet> {

    FormBusSet getByFormKey(String formKey);

}
