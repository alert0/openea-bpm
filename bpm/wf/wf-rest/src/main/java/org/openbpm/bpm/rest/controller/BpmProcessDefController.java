package org.openbpm.bpm.rest.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.util.EnumUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.rest.GenericController;
import org.openbpm.base.rest.util.RequestUtil;
import org.openbpm.bpm.api.engine.action.button.ButtonFactory;
import org.openbpm.bpm.api.model.def.BpmDataModel;
import org.openbpm.bpm.api.model.def.BpmVariableDef;
import org.openbpm.bpm.api.model.nodedef.Button;
import org.openbpm.bpm.api.service.BpmProcessDefService;
import org.openbpm.bpm.core.manager.BpmDefinitionManager;
import org.openbpm.bpm.core.model.BpmDefinition;
import org.openbpm.bpm.engine.model.DefaultBpmProcessDef;
import org.openbpm.bus.api.model.IBusinessColumn;
import org.openbpm.bus.api.model.IBusinessObject;
import org.openbpm.bus.api.service.IBusinessDataService;
import org.openbpm.bus.api.service.IBusinessObjectService;
import org.openbpm.org.api.constant.GroupTypeConstant;
import org.openbpm.org.api.service.GroupService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping({"/bpm/processDef/"})
@RestController
public class BpmProcessDefController extends GenericController {
    @Resource
    IBusinessDataService bizDataService;
    @Resource
    BpmDefinitionManager bpmDefinitionManager;
    @Resource
    BpmProcessDefService bpmProcessDefService;
    @Autowired
    IBusinessObjectService businessObjectService;
    @Resource
    GroupService userGroupService;

    @RequestMapping({"getDefaultNodeBtns"})
    public List<Button> getDefaultNodeBtns(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String nodeId = RequestUtil.getString(request, "nodeId");
        return ButtonFactory.generateButtons(this.bpmProcessDefService.getBpmNodeDef(RequestUtil.getString(request, "defId"), nodeId), Boolean.valueOf(RequestUtil.getBoolean(request, "isDefault", false)).booleanValue());
    }

    @RequestMapping({"variablesTree"})
    public Object variablesTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String defId = RequestUtil.getString(request, "defId");
        String flowKey = RequestUtil.getString(request, "flowKey");
        if (StringUtil.isEmpty(defId)) {
            defId = this.bpmDefinitionManager.getByKey(flowKey).getId();
        }
        DefaultBpmProcessDef bpmProcessDef = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(defId);
        JSONArray treeJA = new JSONArray();
        List<BpmDataModel> boDefs = bpmProcessDef.getDataModelList();
        if (CollectionUtil.isNotEmpty(boDefs)) {
            for (BpmDataModel boDef : boDefs) {
                treeJA.addAll(this.businessObjectService.boTreeData(boDef.getCode()));
            }
        }
        JSONObject flowVarJson = getFlowVarJson(bpmProcessDef);
        if (flowVarJson != null) {
            treeJA.add(flowVarJson);
        }
        return treeJA;
    }

    private JSONObject getFlowVarJson(DefaultBpmProcessDef procDef) {
        List<BpmVariableDef> variables = procDef.getVariableList();
        JSONObject flowVariable = JSONObject.parseObject("{name:\"流程变量\",icon:\"fa fa-bold dark\",\"nodeType\":\"root\"}");
        JSONArray varList = new JSONArray();
        if (CollectionUtil.isNotEmpty(variables)) {
            for (BpmVariableDef variable : variables) {
                String name = variable.getName();
                variable.setName(variable.getKey());
                JSONObject obj = (JSONObject) JSONObject.toJSON(variable);
                obj.put("nodeType", "var");
                varList.add(obj);
            }
        }
        flowVariable.put("children", varList);
        return flowVariable;
    }

    @RequestMapping({"getGroupTypes"})
    @CatchErr
    public JSONArray getGroupTypes(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return EnumUtil.toJSONArray(GroupTypeConstant.class);
    }

    @RequestMapping({"getSubjectVariable"})
    @CatchErr
    public JSONArray getSubjectVariable(@RequestParam String defId) throws Exception {
        JSONArray jsonArray = new JSONArray();
        BpmDefinition def = (BpmDefinition) this.bpmDefinitionManager.get(defId);
        if (def == null || StringUtil.isEmpty(def.getActDefId())) {
            return new JSONArray();
        }
        List<BpmDataModel> boDefs = this.bpmProcessDefService.getBpmProcessDef(defId).getDataModelList();
        if (CollectionUtil.isNotEmpty(boDefs)) {
            for (BpmDataModel boDef : boDefs) {
                IBusinessObject bo = this.businessObjectService.getFilledByKey(boDef.getCode());
                if (bo == null) {
                    throw new BusinessException("业务对象丢失！请核查[" + boDef.getCode() + "]");
                }
                for (IBusinessColumn column : bo.getRelation().getTable().getColumns()) {
                    JSONObject json = new JSONObject();
                    json.put("name", bo.getName() + "-" + column.getComment());
                    json.put("key", bo.getKey() + "." + column.getKey());
                    jsonArray.add(json);
                }
            }
        }
        jsonArray.add(JSONObject.parseObject("{name:\"发起人\",key:\"startorName\"}"));
        jsonArray.add(JSONObject.parseObject("{name:\"发起时间\",key:\"startDate\"}"));
        jsonArray.add(JSONObject.parseObject("{name:\"流程标题\",key:\"title\"}"));
        return jsonArray;
    }
}
