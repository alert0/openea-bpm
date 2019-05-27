package org.openbpm.bpm.rest.controller;

import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.api.query.QueryOP;
import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.base.db.model.page.PageResult;
import org.openbpm.base.rest.BaseController;
import org.openbpm.bpm.core.manager.BpmDefinitionManager;
import org.openbpm.bpm.core.model.BpmDefinition;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping({"/bpm/definition"})
@RestController
public class BpmDefinitionController extends BaseController<BpmDefinition> {
    @Resource
    BpmDefinitionManager bpmDefinitionManager;

    @RequestMapping({"listJson"})
    public PageResult listJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
        QueryFilter queryFilter = getQueryFilter(request);
        queryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL);
        return new PageResult(this.bpmDefinitionManager.query(queryFilter));
    }

    @RequestMapping({"save"})
    @CatchErr(value = "保存流程定义失败", write2response = true)
    public ResultMsg<String> save(@RequestBody BpmDefinition bpmDefinition) throws Exception {
        this.bpmDefinitionManager.create(bpmDefinition);
        return getSuccessResult(bpmDefinition.getActModelId(), "创建成功");
    }

    @RequestMapping({"clearSysCache"})
    @CatchErr("清除缓存失败")
    public ResultMsg<String> clearCache() throws Exception {
        AppUtil.getCache().clearAll();
        return getSuccessResult("成功清除所有系统缓存");
    }

    protected String getModelDesc() {
        return "流程定义";
    }
}
