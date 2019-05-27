package org.openbpm.bpm.core.manager.impl;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import org.openbpm.base.core.id.IdUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.core.util.ThreadMsgUtil;
import org.openbpm.bpm.core.dao.BpmDefinitionDao;
import org.openbpm.bpm.core.manager.BpmDefOverallManager;
import org.openbpm.bpm.core.manager.BpmDefinitionManager;
import org.openbpm.bpm.core.model.BpmDefinition;
import org.openbpm.bpm.core.model.overallview.BpmOverallView;
import org.openbpm.bpm.core.model.overallview.OverallViewExport;
import org.openbpm.bpm.core.model.overallview.OverallViewImportXml;
import org.openbpm.bpm.core.util.XmlCovertUtil;
import org.openbpm.sys.util.ContextUtil;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BpmDefOverallManagerImpl implements BpmDefOverallManager {
    protected Logger LOG = LoggerFactory.getLogger(getClass());
    @Resource
    BpmDefinitionDao bpmDefinitionDao;
    @Resource
    BpmDefinitionManager bpmDefinitionManager;
    @Autowired
    RepositoryService repositoryService;

    public BpmOverallView getBpmOverallView(String defId) {
        BpmDefinition def = (BpmDefinition) this.bpmDefinitionManager.get(defId);
        BpmOverallView overallView = new BpmOverallView();
        overallView.setDefId(def.getId());
        overallView.setBpmDefinition(def);
        overallView.setDefSetting(JSON.parseObject(def.getDefSetting()));
        return overallView;
    }

    public void saveBpmOverallView(BpmOverallView overAllView) {
        BpmDefinition def = overAllView.getBpmDefinition();
        def.setDefSetting(overAllView.getDefSetting().toJSONString());
        this.bpmDefinitionManager.update(def);
    }

    public Map<String, List<BpmOverallView>> importPreview(String flowXml) throws Exception {
        OverallViewImportXml voerallViewExport = (OverallViewImportXml) XmlCovertUtil.covert2Object(flowXml, OverallViewImportXml.class);
        Map<String, List<BpmOverallView>> map = new HashMap<>();
        for (OverallViewExport overallViewExport : voerallViewExport.getBpmXmlList()) {
            List<BpmOverallView> listAllView = new ArrayList<>();
            BpmOverallView overallView = new BpmOverallView();
            BpmDefinition definition = overallViewExport.getBpmDefinition();
            overallView.setIsUpdateVersion(Boolean.valueOf(true));
            overallView.setBpmDefinition(definition);
            overallView.setPermission(overallViewExport.getPermission());
            overallView.setBpmnXml(overallViewExport.getBpmnXml());
            overallView.setDefSetting(JSON.parseObject(definition.getDefSetting()));
            overallView.setModelJson(overallViewExport.getModelEditorJson());
            listAllView.add(overallView);
            BpmDefinition def = this.bpmDefinitionManager.getByKey(overallViewExport.getBpmDefinition().getKey());
            if (def != null) {
                listAllView.add(getBpmOverallView(def.getId()));
            }
            map.put(overallViewExport.getBpmDefinition().getName(), listAllView);
        }
        return map;
    }

    public void importSave(List<BpmOverallView> overAllViewList) {
        ThreadMsgUtil.addMsg("流程导入开始,共：" + overAllViewList.size() + "个流程需要导入");
        for (BpmOverallView overAllView : overAllViewList) {
            if (checkOverAllView(overAllView)) {
                BpmDefinition newDefinition = overAllView.getBpmDefinition();
                BpmDefinition existDefinition = this.bpmDefinitionManager.getByKey(newDefinition.getKey());
                if (existDefinition == null) {
                    try {
                        createOrUpVersionDefinition(overAllView, null);
                        ThreadMsgUtil.addMsg(String.format("流程:“%s” key:【%s】创建导入成功！", new Object[]{newDefinition.getName(), newDefinition.getKey()}));
                    } catch (UnsupportedEncodingException e) {
                        this.LOG.error("流程导入异常，utf-8 字符流获取失败！ 不支持的字符集", e);
                        ThreadMsgUtil.addMsg(String.format("流程导入失败“%s”key:【%s】导入失败 BPMN XML 转流失败！", new Object[]{newDefinition.getName(), newDefinition.getKey()}));
                    }
                } else if (overAllView.getIsUpdateVersion().booleanValue()) {
                    try {
                        createOrUpVersionDefinition(overAllView, existDefinition);
                        ThreadMsgUtil.addMsg(String.format("对流程“%s”key:【%s】进行版本升级成功！", new Object[]{newDefinition.getName(), newDefinition.getKey()}));
                    } catch (UnsupportedEncodingException e) {
                        this.LOG.error("流程导入异常，utf-8 字符流获取失败！ 不支持的字符集", e);
                    }
                } else {
                    if (StringUtil.isNotEmpty(existDefinition.getId())) {
                        copyXmlDefinition(existDefinition, newDefinition);
                        existDefinition.setDefSetting(overAllView.getDefSetting().toJSONString());
                    }
                    try {
                    ProcessDefinition bpmnProcessDef = this.repositoryService.getProcessDefinition(existDefinition.getActDefId());
                    this.repositoryService.addModelEditorSource(existDefinition.getActModelId(), overAllView.getModelJson().getBytes("utf-8"));
                    this.bpmDefinitionDao.updateActResourceEntity(bpmnProcessDef.getDeploymentId(), existDefinition.getKey() + ".bpmn20.xml", overAllView.getBpmnXml().getBytes("utf-8"));
                    this.bpmDefinitionManager.update(existDefinition);
                    ThreadMsgUtil.addMsg(String.format("对流程“%s”key:【%s】进行更新成功！", new Object[]{newDefinition.getName(), existDefinition.getKey()}));
                    } catch (UnsupportedEncodingException e) {
                        this.LOG.error("流程导入异常，utf-8 字符流获取失败！ 不支持的字符集", e);
                    }
                }
            }
        }
    }

    private void createOrUpVersionDefinition(BpmOverallView overAllView, BpmDefinition existDefinition) throws UnsupportedEncodingException {
        BpmDefinition bpmDefinition = overAllView.getBpmDefinition();
        Deployment deployment = this.repositoryService.createDeployment().name(bpmDefinition.getKey()).addString(bpmDefinition.getKey() + ".bpmn20.xml", overAllView.getBpmnXml()).deploy();
        ProcessDefinition proDefinition = (ProcessDefinition) this.repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        String modelId = this.bpmDefinitionManager.createActModel(bpmDefinition);
        Model model = this.repositoryService.getModel(modelId);
        model.setDeploymentId(deployment.getId());
        this.repositoryService.saveModel(model);
        this.repositoryService.addModelEditorSource(modelId, overAllView.getModelJson().getBytes("utf-8"));
        int version = 0;
        String newDefId = IdUtil.getSuid();
        if (existDefinition != null) {
            existDefinition.setIsMain("N");
            existDefinition.setMainDefId(newDefId);
            this.bpmDefinitionManager.update(existDefinition);
            version = existDefinition.getVersion().intValue() + 1;
        }
        bpmDefinition.setId(newDefId);
        bpmDefinition.setIsMain("Y");
        bpmDefinition.setRev(Integer.valueOf(0));
        bpmDefinition.setVersion(Integer.valueOf(version));
        bpmDefinition.setCreateBy(ContextUtil.getCurrentUser().getUserId());
        bpmDefinition.setCreateTime(new Date());
        bpmDefinition.setDefSetting(overAllView.getDefSetting().toJSONString());
        bpmDefinition.setActDefId(proDefinition.getId());
        bpmDefinition.setActDeployId(deployment.getId());
        bpmDefinition.setActModelId(modelId);
        this.bpmDefinitionDao.create(bpmDefinition);
    }

    private boolean checkOverAllView(BpmOverallView overAllView) {
        BpmDefinition newDefinition = overAllView.getBpmDefinition();
        if (StringUtil.isEmpty(newDefinition.getKey())) {
            ThreadMsgUtil.addMsg(String.format("流程导入失败“%s”key:【%s】导入失败 BpmDefinition KEY 不能为空 ！", new Object[]{newDefinition.getName(), newDefinition.getKey()}));
            return false;
        } else if (!StringUtil.isEmpty(overAllView.getBpmnXml())) {
            return true;
        } else {
            ThreadMsgUtil.addMsg(String.format("流程导入失败“%s”key:【%s】导入失败 BPMN XML 为空 ！", new Object[]{newDefinition.getName(), newDefinition.getKey()}));
            return false;
        }
    }

    private void copyXmlDefinition(BpmDefinition existDefinition, BpmDefinition newDefinition) {
        existDefinition.setName(newDefinition.getName());
        existDefinition.setDesc(newDefinition.getDesc());
        existDefinition.setTypeId(newDefinition.getTypeId());
        existDefinition.setDefSetting(newDefinition.getDefSetting());
        existDefinition.setSupportMobile(newDefinition.getSupportMobile());
    }

    public Map<String, String> exportBpmDefinitions(String... defIds) throws Exception {
        Map<String, String> exportFiles = new HashMap<>();
        OverallViewImportXml overallViewImportXml = new OverallViewImportXml();
        for (String defId : defIds) {
            BpmDefinition def = (BpmDefinition) this.bpmDefinitionManager.get(defId);
            if (def == null || StringUtil.isEmpty(def.getActDefId())) {
                this.LOG.info("defId : {} 非可用流程，已经跳过导出！", defId);
            } else {
                OverallViewExport importXml = new OverallViewExport();
                importXml.setBpmDefinition(def);
                importXml.setBpmnXml(IoUtil.read(this.repositoryService.getResourceAsStream(def.getActDeployId(), def.getKey() + ".bpmn20.xml"), "utf-8"));
                importXml.setModelEditorJson(new String(this.repositoryService.getModelEditorSource(def.getActModelId()), "utf-8"));
                overallViewImportXml.addOverallViewExport(importXml);
            }
        }
        exportFiles.put("agilebpm-flow.xml", XmlCovertUtil.covert2Xml(overallViewImportXml));
        return exportFiles;
    }
}
