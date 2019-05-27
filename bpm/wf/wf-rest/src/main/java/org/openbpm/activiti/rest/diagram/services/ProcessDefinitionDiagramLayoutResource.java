package org.openbpm.activiti.rest.diagram.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProcessDefinitionDiagramLayoutResource extends BaseProcessDefinitionDiagramLayoutResource {
    @RequestMapping(method = {RequestMethod.GET}, produces = {"application/json"}, value = {"/process-definition/{processDefinitionId}/diagram-layout"})
    public ObjectNode getDiagram(@PathVariable String processDefinitionId) {
        return getDiagramNode(null, processDefinitionId);
    }
}
