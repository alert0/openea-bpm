package org.openbpm.sys.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.sys.core.manager.SysTreeManager;
import org.openbpm.sys.core.manager.SysTreeNodeManager;
import org.openbpm.sys.core.model.SysTreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.core.id.IdUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.rest.GenericController;
import org.openbpm.base.rest.util.RequestUtil;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 * 描述：sysTreeNode层的controller
 * </pre>
 */
@RestController
@RequestMapping("/sys/sysTreeNode/")
public class SysTreeNodeController extends GenericController {
    @Autowired
    SysTreeManager sysTreeManager;
    @Autowired
    SysTreeNodeManager sysTreeNodeManager;

    /**
     * <pre>
     * sysTreeEdit.html的saveNode后端
     * 保存树节点
     * </pre>
     * @param sysTreeNode
     * @throws Exception
     */
    @RequestMapping("save")
    @CatchErr(write2response = true, value = "保存系统树节点失败")
    public ResultMsg<SysTreeNode> save(@RequestBody SysTreeNode sysTreeNode){
        if (StringUtil.isEmpty(sysTreeNode.getId())) {
            sysTreeNode.setId(IdUtil.getSuid());
            handleNewSysTreeNode(sysTreeNode);
            sysTreeNodeManager.create(sysTreeNode);
        } else {
            sysTreeNodeManager.update(sysTreeNode);
        }
      return  getSuccessResult(sysTreeNode,"保存系统树节点成功");
    }

    /**
     * <pre>
     * 获取sysTreeNode的后端
     * </pre>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getNodes")
    @ResponseBody
    public List<SysTreeNode> getNodes(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<SysTreeNode> nodes = null;
        String treeId = RequestUtil.getString(request, "treeId");
        String key = RequestUtil.getString(request, "nodeKey");
        String treeKey = RequestUtil.getString(request, "treeKey");
        if (StringUtil.isNotEmpty(treeKey) && StringUtil.isEmpty(treeId)) {
            treeId = sysTreeManager.getByKey(treeKey).getId();
        }

        if (StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(treeId)) {
            SysTreeNode node = sysTreeNodeManager.getByTreeIdAndKey(treeId, key);
            nodes = sysTreeNodeManager.getStartWithPath(node.getPath());
        } else if (StringUtil.isNotEmpty(treeId)) {
            nodes = sysTreeNodeManager.getByTreeId(treeId);
        }
        return nodes;
    }

    /**
     * <pre>
     * 批量删除
     * </pre>
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("remove")
    @CatchErr(write2response = true, value = "删除系统树节点失败")
    public ResultMsg<String> remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] aryIds = RequestUtil.getStringAryByStr(request, "id");
        for (String id : aryIds) {
        	SysTreeNode node = sysTreeNodeManager.get(id);
            sysTreeNodeManager.removeByPath(node.getPath()+"%");
        }
        return getSuccessResult("删除系统树节点成功");
    }

    /**
     * <pre>
     * 处理一下新节点的数据
     * </pre>
     *
     * @param sysTreeNode
     */
    private void handleNewSysTreeNode(SysTreeNode sysTreeNode) {
        // 新增时处理一下path
        if (StringUtil.isNotEmpty(sysTreeNode.getPath())) {
            sysTreeNode.setPath(sysTreeNode.getPath() + sysTreeNode.getId() + ".");
        } else {
            sysTreeNode.setPath(sysTreeNode.getId() + ".");
        }

        // 新增处理sn
        // 获取同级节点
        List<SysTreeNode> nodes = sysTreeNodeManager.getByParentId(sysTreeNode.getParentId());
        sysTreeNode.setSn(nodes.size() + 1);
    }
}
