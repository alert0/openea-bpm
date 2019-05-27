package org.openbpm.form.model.custdialog;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * <pre>
 * 描述：自定义对话框列表数据时的展示字段
 * </pre>
 */
@Data
public class FormCustDialogDisplayField implements Serializable {
    /**
     * 字段名
     */
    @NotEmpty
    private String columnName;
    /**
     * 显示名
     */
    @NotEmpty
    private String showName;
    /**
     * 格式化
     */
    private String formatter;



}
