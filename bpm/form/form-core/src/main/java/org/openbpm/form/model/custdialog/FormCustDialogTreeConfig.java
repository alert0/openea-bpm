package org.openbpm.form.model.custdialog;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * <pre>
 * 描述：自定义对话框树形的配置信息
 * </pre>
 */
@Data
public class FormCustDialogTreeConfig implements Serializable {
    /**
     * 树的节点ID字段
     */
    @NotEmpty
    private String id;
    /**
     * 节点的父ID字段
     */
    @NotEmpty
    private String pid;
    /**
     * 父id的初始化值，加载树时使用
     */
    private String pidInitVal;
    /**
     * 父id的初始化值是否配置的是脚本
     */
    private boolean pidInitValScript;
    /**
     * 用来显示的字段
     */
    @NotEmpty
    private String showColumn;


}
