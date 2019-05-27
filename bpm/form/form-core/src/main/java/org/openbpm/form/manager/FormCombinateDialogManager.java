package org.openbpm.form.manager;

import org.openbpm.base.manager.Manager;
import org.openbpm.form.model.FormCombinateDialog;

/**
 * <pre>
 * 描述：combinate_dialog 处理接口
 * </pre>
 */
public interface FormCombinateDialogManager extends Manager<String, FormCombinateDialog> {
    FormCombinateDialog getByAlias(String alias);
}
