package org.openbpm.bpm.core.model.overallview;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import org.openbpm.bpm.core.model.BpmDefinition;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "agilebpmXml")
public class OverallViewExport {

    @XmlElement(name = "bpmDefinition", type = BpmDefinition.class)
    private BpmDefinition bpmDefinition;
    @XmlElement(name = "bpmnXml")
    private String bpmnXml;
    @XmlElement(name = "modelEditorJson")
    private String modelEditorJson;
    @XmlElement(name = "permission")
    private JSONArray permission = new JSONArray();

}
