package org.openbpm.form.model.custdialog;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * <pre>
 * 描述：自定义对话框的条件字段
 * </pre>
 */
@Data
public class FormCustDialogConditionField implements Serializable {
    /**
     * 字段名
     */
    @NotEmpty
    private String columnName;
    /**
     * 字段的类型，枚举在org.openbpm.base.db.model.Column.TYPE
     */
    @NotEmpty
    private String dbType;
    /**
     * 显示名
     */
    @NotEmpty
    private String showName;
    /**
     * 条件
     */
    @NotEmpty
    private String condition;
    /**
     * 值来源 枚举 FormCustDialogConditionFieldValueSource
     */
    @NotEmpty
    private String valueSource;
    /**
     * 值
     */
    @NotEmpty
    private Value value;


    /**
     * <pre>
     * 描述：值对象
     * </pre>
     */
    @Data
    public static class Value implements Serializable {
        /**
         * 控件类型
         */
        private String ctrlType;
        /**
         * 控件key
         */
        private String ctrlKey;
        /**
         * 控件返回
         */
        private String ctrlReturn;
        /**
         * 文本
         */
        private String text;


    }
}
