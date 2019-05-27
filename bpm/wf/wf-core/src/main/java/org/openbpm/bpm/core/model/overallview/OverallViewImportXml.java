package org.openbpm.bpm.core.model.overallview;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "agilebpmXmlList")
public class OverallViewImportXml {
    @XmlElements({@XmlElement(name = "agilebpmXml", type = OverallViewExport.class)})
    private List<OverallViewExport> bpmXmlList = new ArrayList();

    public void addOverallViewExport(OverallViewExport def) {
        this.bpmXmlList.add(def);
    }
}
