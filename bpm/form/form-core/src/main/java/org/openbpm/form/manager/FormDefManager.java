package org.openbpm.form.manager;

import org.openbpm.base.manager.Manager;
import org.openbpm.form.model.FormDef;

import java.util.List;
import java.util.Map;

/**
 * 表单 Manager处理接口
 *
 */
public interface FormDefManager extends Manager<String, FormDef> {

    /**
     * <pre>
     * 根据别名获取表单
     * </pre>
     *
     * @param key
     * @return
     */
    FormDef getByKey(String key);

    /**
     * <pre>
     * 处理保存表单的html到文件的逻辑
     * 备份用
     * </pre>
     *
     * @param formDef
     */
    void saveBackupHtml(FormDef formDef);

    /**
     * <pre>
     * 读取备份文件中的表单html
     * </pre>
     *
     * @param formDef
     * @return
     */
    String getBackupHtml(FormDef formDef);

    Map<String, String> exportForms(List<String> id, boolean b);

    void importByFormXml(String formXmlStr);
}
