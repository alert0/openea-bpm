package org.openbpm.form.model;

import lombok.Data;

import java.io.Serializable;

/**
 * <pre>
 * 描述：表单模版 entity对象
 * </pre>
 */
@Data
public class FormTemplate implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 别名
     */
    private String key;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String desc;
    /**
     * 类型 枚举 FormTemplateType
     */
    private String type;
    
    /**
     * FormType 表单的分类  pc、mobile、vuepc 等不同的表单分类  默认 pc
     */
    private String formType;
    /**
     * html内容
     */
    private String html;
    /**
     * 是否可以编辑 不可编辑也相当于系统内置
     */
    private boolean editable;



}