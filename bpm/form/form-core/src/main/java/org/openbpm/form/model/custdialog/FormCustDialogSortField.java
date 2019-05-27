package org.openbpm.form.model.custdialog;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * <pre>
 * 描述：自定义对话框的排序字段
 * </pre>
 */
@Data
public class FormCustDialogSortField implements Serializable {
    /**
     * 字段名
     */
    @NotEmpty
    private String columnName;
    /**
     * 排序类型 org.openbpm.base.api.constant.Direction.key
     */
    @NotEmpty
    private String sortType;


}
