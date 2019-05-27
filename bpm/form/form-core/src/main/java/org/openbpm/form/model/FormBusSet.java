package org.openbpm.form.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;

import org.openbpm.base.api.model.IDModel;


/**
 * <pre>
 * 描述：form_bus_set 实体对象
 * </pre>
 */
@Data
public class FormBusSet implements IDModel {

    /**
     * 主键
     */
    @XmlAttribute(name = "id")
    protected String id;

    /**
     * formKey
     */
    @XmlAttribute(name = "formKey")
    protected String formKey;

    /**
     * JavaScript前置脚本
     */
    @XmlElement(name = "jsPreScript")
    protected String jsPreScript;

    /**
     * JavaScript后置脚本
     */
    @XmlElement(name = "jsAfterScript")
    protected String jsAfterScript;

    /**
     * 保存前置脚本
     */
    @XmlElement(name = "preScript")
    protected String preScript;

    /**
     * 保存后置脚本
     */
    @XmlElement(name = "afterScript")
    protected String afterScript;

    /**
     * 展示前Java脚本
     */
    @XmlElement(name = "showScript")
    protected String showScript;

    /**
     * 是否树形列表
     */
    @XmlAttribute(name = "isTreeList")
    protected Short isTreeList;

    /**
     * {idKey:"idKEY名称",pIdKey:"",name:"显示名称",title,rootPid}
     */
    @XmlElement(name = "treeConf")
    protected String treeConf;


}