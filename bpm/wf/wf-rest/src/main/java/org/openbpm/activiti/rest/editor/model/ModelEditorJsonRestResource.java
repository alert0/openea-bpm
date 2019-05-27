package org.openbpm.activiti.rest.editor.model;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.engine.context.BpmContext;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.bpm.core.manager.BpmDefinitionManager;
import org.openbpm.bpm.core.model.BpmDefinition;
import javax.annotation.Resource;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModelEditorJsonRestResource {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ModelEditorJsonRestResource.class);
    @Resource
    private BpmDefinitionManager bpmDefinitionManager;
    @Autowired
    private BpmProcessDefService bpmProcessDefService;
    @Autowired
    private RepositoryService repositoryService;

    @ResponseBody
    @RequestMapping(method = {RequestMethod.GET}, value = {"/model/{modelId}/json"})
    public JSONObject getEditorJson(@PathVariable String modelId) {
        JSONObject json = null;
        BpmContext.cleanTread();
        Model model = this.repositoryService.getModel(modelId);
        if (model != null) {
            try {
                if (StringUtils.isNotEmpty(model.getMetaInfo())) {
                    json = JSONObject.parseObject(model.getMetaInfo());
                } else {
                    JSONObject json2 = new JSONObject();
                    try {
                        json2.put("name", model.getName());
                        json = json2;
                    } catch (Exception e) {
                        e = e;
                        JSONObject jSONObject = json2;
                        LOGGER.error("Error creating model JSON", e);
                        throw new ActivitiException("Error creating model JSON", e);
                    }
                }
                json.put("modelId", model.getId());
                JSONObject editorModelJson = JSONObject.parseObject(new String(this.repositoryService.getModelEditorSource(model.getId()), "utf-8"));
                BpmDefinition def = this.bpmDefinitionManager.getDefByActModelId(modelId);
                JSONObject bpmDefSetting = new JSONObject();
                if (StringUtil.isNotEmpty(def.getActDefId())) {
                    bpmDefSetting = this.bpmProcessDefService.getBpmProcessDef(def.getId()).getJson();
                }
                def.setDefSetting(null);
                bpmDefSetting.put("bpmDefinition", def);
                json.put("bpmDefSetting", bpmDefSetting);
                if (!editorModelJson.containsKey("properties")) {
                    JSONObject initJson = new JSONObject();
                    initJson.put("process_id", model.getKey());
                    initJson.put("name", model.getName());
                    editorModelJson.put("properties", initJson);
                }
                json.put("model", editorModelJson);
            } catch (Exception e) {
                LOGGER.error("Error creating model JSON", e);
                throw new ActivitiException("Error creating model JSON", e);
            }
        }
        return json;
    }
}
