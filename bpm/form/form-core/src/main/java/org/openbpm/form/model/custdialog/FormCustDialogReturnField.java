package org.openbpm.form.model.custdialog;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * <pre>
 * 描述：自定义对话框的返回字段
 * </pre>
 */
@Data
public class FormCustDialogReturnField implements Serializable {
    /**
     * 字段名
     */
    @NotEmpty
    private String columnName;
    /**
     * 返回名
     */
    @NotEmpty
    private String returnName;

}
