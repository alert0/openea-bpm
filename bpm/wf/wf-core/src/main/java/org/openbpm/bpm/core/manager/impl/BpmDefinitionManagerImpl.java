package org.openbpm.bpm.core.manager.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.api.exception.BusinessMessage;
import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.api.query.QueryOP;
import org.openbpm.base.core.id.IdUtil;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.db.model.query.DefaultQueryFilter;
import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.bpm.api.engine.event.BpmDefinitionUpdateEvent;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.bpm.core.dao.BpmDefinitionDao;
import org.openbpm.bpm.core.manager.BpmDefinitionManager;
import org.openbpm.bpm.core.manager.BpmInstanceManager;
import org.openbpm.bpm.core.model.BpmDefinition;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import org.openbpm.bpm.engine.parser.FlowConfigConstants.FORM;
import org.openbpm.sys.api.constant.EnvironmentConstant;
import org.openbpm.sys.api.constant.RightsObjectConstants;
import org.openbpm.sys.api.service.SysAuthorizationService;
import org.openbpm.sys.util.ContextUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.bpmn.deployer.BpmnDeployer;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("bpmDefinitionManager")
public class BpmDefinitionManagerImpl extends BaseManager<String, BpmDefinition> implements BpmDefinitionManager {
    protected Logger LOG = LoggerFactory.getLogger(getClass());
    @Resource
    BpmDefinitionDao bpmDefinitionDao;
    @Resource
    BpmInstanceManager bpmInstanceManager;
    @Resource
    BpmProcessDefService bpmProcessDefService;
    @Resource
    ProcessEngineConfiguration processEngineConfiguration;
    @Resource
    RepositoryService repositoryService;
    @Resource
    SysAuthorizationService sysAuthorizationService;

    public void create(BpmDefinition bpmDefinition) {
        if (StringUtil.isNotEmpty(bpmDefinition.getId())) {
            update(bpmDefinition);
        } else if (CollectionUtil.isNotEmpty(this.bpmDefinitionDao.getByKey(bpmDefinition.getKey()))) {
            throw new BusinessMessage("流程定义Key重复：" + bpmDefinition.getKey());
        } else {
            bpmDefinition.setIsMain("Y");
            bpmDefinition.setStatus("draft");
            bpmDefinition.setVersion(Integer.valueOf(1));
            String defId = IdUtil.getSuid();
            bpmDefinition.setId(defId);
            bpmDefinition.setMainDefId(defId);
            bpmDefinition.setActModelId(createActModel(bpmDefinition));
            this.bpmDefinitionDao.create(bpmDefinition);
        }
    }

    public String createActModel(BpmDefinition bpmDefinition) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.set("stencilset", stencilSetNode);
            Model modelData = this.repositoryService.newModel();
            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(FORM.NAME, bpmDefinition.getName());
            modelObjectNode.put("revision", 1);
            modelObjectNode.put("key", bpmDefinition.getKey());
            modelObjectNode.put("description", bpmDefinition.getDesc());
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName(bpmDefinition.getName());
            modelData.setKey(bpmDefinition.getKey());
            this.repositoryService.saveModel(modelData);
            this.repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
            return modelData.getId();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("创建activiti流程定义失败！", e);
        }
    }

    public void updateBpmnModel(Model model, Map<String, String> values) throws Exception {
        String bpmDefSettingJSON = (String) values.get("bpmDefSetting");
        BpmDefinition bpmDef = getDefByActModelId(model.getId());
        bpmDef.setName((String) values.get(FORM.NAME));
        bpmDef.setDesc((String) values.get("description"));
        bpmDef.setDefSetting(bpmDefSettingJSON);
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(new ObjectMapper().readTree(((String) values.get("json_xml")).getBytes("utf-8")));
        if (CollectionUtil.isEmpty(bpmnModel.getProcesses())) {
            throw new BusinessMessage("请绘制流程图后再保存！");
        }
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
        boolean publish = Boolean.parseBoolean((String) values.get("publish"));
        if (StringUtil.isEmpty(bpmDef.getActDefId()) || publish) {
            bpmDef = deploy(bpmDef, model, values, bpmnBytes);
        } else {
            saveModelInfo(model, values);
            updateProcessDef(bpmDef, model, bpmnBytes);
        }
        DefaultBpmProcessDef def = (DefaultBpmProcessDef) this.bpmProcessDefService.initBpmProcessDef(bpmDef);
        if (!"deploy".equals(bpmDef.getStatus())
                || !"deploy".equals(def.getExtProperties().getStatus())
                || AppUtil.getCtxEnvironment().contains(EnvironmentConstant.PROD.key())) {
            if (StringUtil.isEmpty(bpmDef.getStatus()) || !bpmDef.getStatus().equals(def.getExtProperties().getStatus())
                    || !bpmDef.getSupportMobile().equals(def.getExtProperties().getSupportMobile())) {
                bpmDef.setStatus(def.getExtProperties().getStatus());
                bpmDef.setSupportMobile(def.getExtProperties().getSupportMobile());
                this.bpmDefinitionDao.update(bpmDef);
            }
            publishEvent(bpmDef);
            return;
        }
        throw new BusinessMessage("除了生产环境外，已发布状态的流程禁止修改！");
    }

    private void saveModelInfo(Model model, Map<String, String> values) {
        try {
            byte[] jsonXml = ((String) values.get("json_xml")).getBytes("utf-8");
            TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(((String) values.get("svg_xml")).getBytes("utf-8")));
            PNGTranscoder transcoder = new PNGTranscoder();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            transcoder.transcode(input, new TranscoderOutput(outStream));
            byte[] svg_xml = outStream.toByteArray();
            this.repositoryService.saveModel(model);
            this.repositoryService.addModelEditorSourceExtra(model.getId(), svg_xml);
            this.repositoryService.addModelEditorSource(model.getId(), jsonXml);
            outStream.close();
        } catch (Exception e) {
            throw new BusinessException("保存Model信息失败！", e);
        }
    }

    private void updateProcessDef(BpmDefinition definition, Model model, byte[] bpmnBytes) throws JsonProcessingException, IOException {
        ProcessDefinition bpmnProcessDef = this.repositoryService.getProcessDefinition(definition.getActDefId());
        ProcessEngineConfigurationImpl conf = (ProcessEngineConfigurationImpl) this.processEngineConfiguration;
        Context.setProcessEngineConfiguration(conf);
        DeploymentManager deploymentManager = conf.getDeploymentManager();
        BpmnDeployer deployer = (BpmnDeployer) deploymentManager.getDeployers().get(0);
        BpmnParse bpmnParse = deployer.getBpmnParser().createParse().sourceInputStream(new ByteArrayInputStream(bpmnBytes)).setSourceSystemId(model.getKey() + ".bpmn20.xml").deployment((DeploymentEntity) this.repositoryService.createDeploymentQuery().deploymentId(definition.getActDeployId()).list().get(0)).name(model.getKey() + ".bpmn20.xml");
        bpmnParse.execute();
        BpmnModel bpmnModel = bpmnParse.getBpmnModel();
        deploymentManager.getBpmnModelCache().add(bpmnProcessDef.getId(), bpmnModel);
        byte[] diagramBytes = IoUtil.readInputStream(this.processEngineConfiguration.getProcessDiagramGenerator().generateDiagram(bpmnModel, "png", this.processEngineConfiguration.getActivityFontName(), this.processEngineConfiguration.getLabelFontName(), this.processEngineConfiguration.getAnnotationFontName(), this.processEngineConfiguration.getClassLoader()), null);
        this.bpmDefinitionDao.updateActResourceEntity(bpmnProcessDef.getDeploymentId(), model.getKey() + ".bpmn20.xml", bpmnBytes);
        this.bpmDefinitionDao.updateActResourceEntity(bpmnProcessDef.getDeploymentId(), model.getKey() + "." + bpmnProcessDef.getKey() + ".png", diagramBytes);
        update(definition);
    }

    private BpmDefinition deploy(BpmDefinition definition, Model preModel, Map<String, String> values, byte[] bpmnModelXml) {
        Deployment deployment = this.repositoryService.createDeployment().name(definition.getKey()).addString(definition.getKey() + ".bpmn20.xml", new String(bpmnModelXml)).deploy();
        ProcessDefinition proDefinition = (ProcessDefinition) this.repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        if (proDefinition == null) {
            throw new RuntimeException("error   ");
        }
        if (StringUtil.isEmpty(definition.getActDefId())) {
            definition.setActDefId(proDefinition.getId());
            definition.setActDeployId(deployment.getId());
            update(definition);
            saveModelInfo(preModel, values);
        } else {
            Model model = this.repositoryService.getModel(createActModel(definition));
            model.setDeploymentId(deployment.getId());
            this.repositoryService.saveModel(model);
            saveModelInfo(model, values);
            String newDefId = IdUtil.getSuid();
            BpmDefinition def = (BpmDefinition) get(definition.getId());
            def.setIsMain("N");
            def.setMainDefId(newDefId);
            update(def);
            definition.setId(newDefId);
            definition.setIsMain("Y");
            definition.setRev(Integer.valueOf(0));
            definition.setMainDefId(newDefId);
            definition.setVersion(Integer.valueOf(definition.getVersion().intValue() + 1));
            definition.setCreateBy(ContextUtil.getCurrentUser().getUserId());
            definition.setCreateTime(new Date());
            definition.setActDefId(proDefinition.getId());
            definition.setActDeployId(deployment.getId());
            definition.setActModelId(model.getId());
            this.bpmDefinitionDao.create(definition);
        }
        return definition;
    }

    public BpmDefinition getDefByActModelId(String actModelId) {
        List<BpmDefinition> list = this.bpmDefinitionDao.getDefByActModelId(actModelId);
        if (CollectionUtil.isEmpty(list)) {
            throw new BusinessException("getDefByActModelId 查找失败modelId：" + actModelId);
        }
        if (list.size() > 1) {
            this.LOG.warn("getDefByActModelId 查找多条 modelId：" + actModelId);
        }
        for (BpmDefinition def : list) {
            if ("Y".equals(def.getIsMain())) {
                return def;
            }
        }
        return (BpmDefinition) list.get(0);
    }

    private void publishEvent(BpmDefinition def) {
        for (BpmDefinition defEntity : this.bpmDefinitionDao.getByKey(def.getKey())) {
            AppUtil.publishEvent(new BpmDefinitionUpdateEvent(defEntity));
        }
        AppUtil.publishEvent(new BpmDefinitionUpdateEvent(def));
    }

    public BpmDefinition getDefinitionByActDefId(String actDefId) {
        return this.bpmDefinitionDao.getByActDefId(actDefId);
    }

    public BpmDefinition getByKey(String flowKey) {
        return this.bpmDefinitionDao.getMainByDefKey(flowKey);
    }

    public List<BpmDefinition> getMyDefinitionList(String userId, QueryFilter queryFilter) {
        queryFilter.addParams(this.sysAuthorizationService.getUserRightsSql(RightsObjectConstants.FLOW, userId, "key_"));
        return this.bpmDefinitionDao.getMyDefinitionList(queryFilter);
    }

    public void remove(String entityId) {
        BpmDefinition definition = (BpmDefinition) this.bpmDefinitionDao.get(entityId);
        if (isNotEmptyInstance(definition.getId())) {
            throw new BusinessMessage("该流程定义下存在流程实例，请勿删除！<br> 请清除数据后再来删除");
        }
        for (BpmDefinition def : this.bpmDefinitionDao.getByKey(definition.getKey())) {
            AppUtil.publishEvent(new BpmDefinitionUpdateEvent(def));
            this.bpmDefinitionDao.remove(def.getId());
            if (StringUtil.isNotEmpty(def.getActDeployId())) {
                this.repositoryService.deleteDeployment(def.getActDeployId());
            }
        }
        if (StringUtil.isNotEmpty(definition.getActModelId())) {
            this.repositoryService.deleteModel(definition.getActModelId());
        }
    }

    private boolean isNotEmptyInstance(String defId) {
        DefaultQueryFilter query = new DefaultQueryFilter();
        query.addFilter("def_id_", defId, QueryOP.EQUAL);
        return CollectionUtil.isNotEmpty(this.bpmInstanceManager.query(query));
    }

    public void update(BpmDefinition entity) {
        entity.setUpdateTime(new Date());
        if (this.bpmDefinitionDao.update(entity).intValue() == 0) {
            throw new RuntimeException("流程定义更新失败，当前版本并非最新版本！已经刷新当前服务器缓存，请刷新页面重新修改提交。id:" + entity.getId() + "reversion:" + entity.getRev());
        }
        AppUtil.publishEvent(new BpmDefinitionUpdateEvent(entity));
    }
}
