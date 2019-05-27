package org.openbpm.bpm.service;

import cn.hutool.core.collection.CollectionUtil;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bpm.api.engine.data.result.BpmFlowData;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.model.form.BpmForm;
import org.openbpm.bpm.api.model.form.DefaultForm;
import org.openbpm.bpm.api.model.nodedef.BpmNodeDef;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.bpm.api.service.BpmRightsFormService;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import org.openbpm.bus.api.constant.BusinessPermissionObjType;
import org.openbpm.bus.api.model.IBusinessPermission;
import org.openbpm.bus.api.service.IBusinessPermissionService;
import org.openbpm.form.api.model.FormCategory;
import org.openbpm.form.api.model.FormType;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component("defaultBpmFormService")
public class DefaultBpmRightsFormService implements BpmRightsFormService {
    @Resource
    BpmProcessDefService bpmProcessDefService;
    @Resource
    IBusinessPermissionService businessPermissionService;

    public IBusinessPermission getInstanceFormPermission(BpmFlowData flowData, String nodeId, FormType formType, boolean isReadOnly) {
        BpmForm form;
        String nodeName;
        IBusinessPermission permision = null;
        boolean isMobile = FormType.MOBILE == formType;
        DefaultBpmProcessDef processDef = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(flowData.getDefId());
        if (StringUtil.isEmpty(nodeId)) {
            form = isMobile ? processDef.getInstMobileForm() : processDef.getInstForm();
            nodeId = "instance";
            nodeName = "实例";
        } else {
            BpmNodeDef nodeDef = this.bpmProcessDefService.getBpmNodeDef(flowData.getDefId(), nodeId);
            form = isMobile ? nodeDef.getMobileForm() : nodeDef.getForm();
            nodeName = nodeDef.getName();
        }
        if (form == null || form.isFormEmpty()) {
            form = isMobile ? processDef.getGlobalMobileForm() : processDef.getGlobalForm();
            nodeId = "global";
            nodeName = "全局";
        }
        if (form == null || form.isFormEmpty()) {
            throw new BusinessException(String.format("请配置流程“%s”的“%s”表单", new Object[]{processDef.getName(), nodeName}), BpmStatusCode.FLOW_FORM_LOSE);
        }
        if (FormCategory.INNER.equals(form.getType())) {
            permision = this.businessPermissionService.getByObjTypeAndObjVal(BusinessPermissionObjType.FLOW.getKey(), processDef.getDefKey() + "-" + nodeId, processDef.getDataModelKeys(), true);
            flowData.setPermission(permision.getPermission(isReadOnly));
            flowData.setTablePermission(permision.getTablePermission(isReadOnly));
        }
        DefaultForm bpmForm = new DefaultForm();
        bpmForm.setFormHandler(form.getFormHandler());
        bpmForm.setFormHtml(form.getFormHtml());
        bpmForm.setFormValue(form.getFormValue());
        bpmForm.setName(form.getName());
        bpmForm.setType(form.getType());
        flowData.setForm(bpmForm);
        return permision;
    }

    public IBusinessPermission getNodeSavePermission(String defKey, String nodeId, Set<String> bocodes) {
        String boCodes = null;
        if (CollectionUtil.isNotEmpty(bocodes)) {
            boCodes = StringUtil.join(bocodes);
        }
        return this.businessPermissionService.getByObjTypeAndObjVal(BusinessPermissionObjType.FLOW.getKey(), defKey + "-" + nodeId, boCodes, true);
    }
}
