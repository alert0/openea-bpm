package org.openbpm.form.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import org.openbpm.form.api.model.IFormDef;

import java.util.List;

/**
 * 此类用于表单的导入导出。
 */
@Data
@XmlRootElement(name = "bpmForms")
@XmlAccessorType(XmlAccessType.FIELD)
public class FormXml {


    @XmlElement(name = "bpmForm", type = FormDef.class)
    private IFormDef bpmForm;

    @XmlElement(name = "formBusSet", type = FormBusSet.class)
    private FormBusSet formBusSet;

    @XmlElement(name = "boCodes")
    private List<String> boCodes;


}
