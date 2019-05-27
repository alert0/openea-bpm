package org.openbpm.activiti.rest.editor.model;

import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.rest.util.RequestUtil;
import org.openbpm.bpm.core.manager.BpmDefinitionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.activiti.bpmn.exceptions.XMLException;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModelSaveRestResource implements ModelDataJsonConstants {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ModelSaveRestResource.class);
    @Autowired
    private BpmDefinitionManager bpmDefinitionManager;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Resource
    RepositoryService repositoryService;

    @RequestMapping(method = {RequestMethod.PUT}, value = {"/model/{modelId}/save"})
    @CatchErr
    @ResponseStatus(HttpStatus.OK)
    public void saveModel(HttpServletResponse response, HttpServletRequest request, @PathVariable String modelId, @RequestBody(required = false) MultiValueMap<String, String> values) throws Exception {
        Map<String, String> params;
        if (values != null) {
            params = new HashMap<>();
            for (String key : values.keySet()) {
                params.put(key, values.getFirst(key));
            }
        } else {
            params = RequestUtil.getParameterValueMap(request, false);
        }
        Model model = this.repositoryService.getModel(modelId);
        ObjectNode modelJson = (ObjectNode) this.objectMapper.readTree(model.getMetaInfo());
        modelJson.put("name", (String) params.get("name"));
        modelJson.put("description", (String) params.get("description"));
        try {
            this.bpmDefinitionManager.updateBpmnModel(model, params);
        } catch (XMLException e) {
            throw new BusinessException("流程图解析失败！不合法的流程图：" + e.getMessage(), e);
        }
    }
}
