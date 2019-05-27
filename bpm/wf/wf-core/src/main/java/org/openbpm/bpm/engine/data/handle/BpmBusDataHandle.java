package org.openbpm.bpm.engine.data.handle;

import cn.hutool.core.map.MapUtil;
import org.openbpm.base.api.exception.BusinessError;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.db.dboper.DbOperator;
import org.openbpm.base.db.dboper.DbOperatorFactory;
import org.openbpm.bpm.api.engine.action.cmd.BaseActionCmd;
import org.openbpm.bpm.api.exception.BpmStatusCode;
import org.openbpm.bpm.api.model.def.BpmDataModel;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.bpm.api.service.BpmRightsFormService;
import org.openbpm.bpm.core.manager.BpmBusLinkManager;
import org.openbpm.bpm.core.manager.BpmInstanceManager;
import org.openbpm.bpm.core.model.BpmBusLink;
import org.openbpm.bpm.core.model.BpmInstance;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import org.openbpm.bus.api.model.IBusinessData;
import org.openbpm.bus.api.model.IBusinessObject;
import org.openbpm.bus.api.model.IBusinessPermission;
import org.openbpm.bus.api.service.IBusinessDataService;
import org.openbpm.bus.api.service.IBusinessObjectService;
import org.openbpm.bus.api.service.IBusinessPermissionService;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BpmBusDataHandle {
    private static Set<String> partions = Collections.synchronizedSet(new HashSet());
    private static int supportPart = -1;
    private static final String tableName = "bpm_bus_link";    //"BPM_BUS_LINK"
    @Resource
    private BpmBusLinkManager bpmBusLinkManager;
    @Resource
    private BpmInstanceManager bpmInstanceManager;
    @Resource
    private BpmProcessDefService bpmProcessDefService;
    @Resource
    private BpmRightsFormService bpmRightsAndFormService;
    @Autowired
    private IBusinessObjectService businessObjectService;
    @Autowired
    private IBusinessPermissionService businessPermissionService;
    @Resource
    private IBusinessDataService iBusinessDataService;

    public Map<String, IBusinessData> getInstanceData(IBusinessPermission businessPermision, BpmInstance instance) {
        Map<String, IBusinessData> dataMap = new HashMap<>();
        BpmInstance topInstance = this.bpmInstanceManager.getTopInstance(instance);
        if (topInstance != null) {
            for (BpmBusLink busLink : this.bpmBusLinkManager.getByInstanceId(topInstance.getId())) {
                IBusinessObject businessObject = this.businessObjectService.getFilledByKey(busLink.getBizCode());
                businessObject.setPermission(businessPermision.getBusObj(busLink.getBizCode()));
                IBusinessData busData = this.iBusinessDataService.loadData(businessObject, busLink.getBizId());
                if (busData == null) {
                    throw new BusinessError(String.format("bizCode[%s] bizId[%s]", new Object[]{busLink.getBizCode(), busLink.getBizId()}), BpmStatusCode.FLOW_BUS_DATA_LOSE);
                }
                dataMap.put(busLink.getBizCode(), busData);
            }
        }
        for (BpmBusLink busLink2 : this.bpmBusLinkManager.getByInstanceId(instance.getId())) {
            IBusinessObject businessObject2 = this.businessObjectService.getFilledByKey(busLink2.getBizCode());
            businessObject2.setPermission(businessPermision.getBusObj(busLink2.getBizCode()));
            IBusinessData busData2 = this.iBusinessDataService.loadData(businessObject2, busLink2.getBizId());
            if (busData2 == null) {
                throw new BusinessError(String.format("bizCode[%s] bizId[%s]", new Object[]{busLink2.getBizCode(), busLink2.getBizId()}), BpmStatusCode.FLOW_BUS_DATA_LOSE);
            }
            dataMap.put(busLink2.getBizCode(), busData2);
        }
        for (BpmDataModel model : this.bpmProcessDefService.getBpmProcessDef(instance.getDefId()).getDataModelList()) {
            String code = model.getCode();
            if (!dataMap.containsKey(code)) {
                IBusinessObject businessObject3 = this.businessObjectService.getFilledByKey(code);
                businessObject3.setPermission(businessPermision.getBusObj(code));
                dataMap.put(code, this.iBusinessDataService.loadData(businessObject3, null));
            }
        }
        return dataMap;
    }

    public Map<String, IBusinessData> getInitData(IBusinessPermission businessPermision, String defId) {
        Map<String, IBusinessData> dataMap = new HashMap<>();
        for (BpmDataModel model : this.bpmProcessDefService.getBpmProcessDef(defId).getDataModelList()) {
            String code = model.getCode();
            IBusinessObject businessObject = this.businessObjectService.getFilledByKey(code);
            businessObject.setPermission(businessPermision.getBusObj(code));
            dataMap.put(code, this.iBusinessDataService.loadData(businessObject, null));
        }
        return dataMap;
    }

    public void saveDataModel(BaseActionCmd actionCmd) {
        Map<String, IBusinessData> boDataMap = actionCmd.getBizDataMap();
        if (!MapUtil.isEmpty(boDataMap)) {
            BpmInstance instance = (BpmInstance) actionCmd.getBpmInstance();
            String nodeId = actionCmd.getNodeId();
            if (StringUtil.isEmpty(nodeId)) {
                nodeId = this.bpmProcessDefService.getStartEvent(instance.getDefId()).getNodeId();
            }
            IBusinessPermission businessPermision = this.bpmRightsAndFormService.getNodeSavePermission(instance.getDefKey(), nodeId, boDataMap.keySet());
            BpmInstance topInstance = this.bpmInstanceManager.getTopInstance(instance);
            HashSet hashSet = new HashSet();
            if (topInstance != null) {
                DefaultBpmProcessDef topDef = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(topInstance.getDefId());
                List<BpmBusLink> topBusLinks = this.bpmBusLinkManager.getByInstanceId(topInstance.getId());
                for (BpmDataModel topModel : topDef.getDataModelList()) {
                    String modelCode = topModel.getCode();
                    if (boDataMap.containsKey(modelCode)) {
                        hashSet.add(modelCode);
                        IBusinessData businessData = (IBusinessData) boDataMap.get(modelCode);
                        businessData.getBusTableRel().getBusObj().setPermission(businessPermision.getBusObj(modelCode));
                        this.iBusinessDataService.saveData(businessData);
                        handleBusLink(businessData, modelCode, topInstance, topBusLinks);
                    }
                }
            }
            List<BpmBusLink> busLinkList = this.bpmBusLinkManager.getByInstanceId(instance.getId());
            for (BpmDataModel dataModel : this.bpmProcessDefService.getBpmProcessDef(instance.getDefId()).getDataModelList()) {
                String modelCode2 = dataModel.getCode();
                if (boDataMap.containsKey(modelCode2) && !hashSet.contains(modelCode2)) {
                    IBusinessData businessData2 = (IBusinessData) boDataMap.get(modelCode2);
                    businessData2.getBusTableRel().getBusObj().setPermission(businessPermision.getBusObj(modelCode2));
                    this.iBusinessDataService.saveData(businessData2);
                    handleBusLink(businessData2, modelCode2, instance, busLinkList);
                }
            }
        }
    }

    private void handleBusLink(IBusinessData iBusinessData, String modelCode, BpmInstance instance, List<BpmBusLink> busLinks) {
        for (BpmBusLink link : busLinks) {
            if (link.getBizId().equals(iBusinessData.getPk())) {
                return;
            }
        }
        BpmBusLink busLink = new BpmBusLink();
        busLink.setBizCode(modelCode);
        busLink.setBizId(String.valueOf(iBusinessData.getPk()));
        busLink.setInstId(instance.getId());
        busLink.setDefId(instance.getDefId());
        createPartition(modelCode);
        this.bpmBusLinkManager.create(busLink);
    }

    private void createPartition(String partName) {
        DbOperator dbOperator = DbOperatorFactory.getLocal();
        if (!StringUtil.isEmpty(partName)) {
            if (supportPart == -1) {
                supportPart = dbOperator.supportPartition(tableName) ? 1 : 0;
            }
            if (supportPart != 0 && !partions.contains(partName)) {
                if (dbOperator.isExsitPartition(tableName, partName)) {
                    partions.add(partName);
                    return;
                }
                dbOperator.createPartition(tableName, partName);
                partions.add(partName);
            }
        }
    }
}
