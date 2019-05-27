package org.openbpm.sys.rest.controller;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openbpm.sys.api.service.SerialNoService;
import org.openbpm.sys.core.manager.SerialNoManager;
import org.openbpm.sys.core.model.SerialNo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.api.exception.BusinessMessage;
import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.db.model.page.PageResult;
import org.openbpm.base.rest.GenericController;
import org.openbpm.base.rest.util.RequestUtil;
import org.openbpm.sys.api.constant.SysStatusCode;
import com.github.pagehelper.Page;

/**
 * 流水号生成管理
 */
@RestController
@RequestMapping("/sys/serialNo/")
public class SysSerialNoController extends GenericController {
    @Resource
    SerialNoManager serialNoManager;
    @Resource
    SerialNoService serialNoService;


    /**
     * 流水号生成列表(分页条件查询)数据
     *
     * @param request
     * @param response
     * @return
     * @throws Exception PageJson
     */
    @RequestMapping("listJson")
    public 
    PageResult listJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
        QueryFilter queryFilter = getQueryFilter(request);
        Page<SerialNo> SerialNoList = (Page<SerialNo>) serialNoManager.query(queryFilter);
        return new PageResult(SerialNoList);
    }

    public static void main(String[] args) {

    }

    /**
     * 根据ID获取内容
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getById")
    @CatchErr(write2response = true)
    public ResultMsg<SerialNo> getById(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = RequestUtil.getString(request, "id");
        SerialNo SerialNo = serialNoManager.get(id);
        return getSuccessResult(SerialNo);
    }

    /**
     * 保存流水号生成信息
     *
     * @param SerialNo
     * @throws Exception void
     */
    @RequestMapping("save")
    @CatchErr
    public ResultMsg<String> save(@RequestBody SerialNo SerialNo) throws Exception {
        String resultMsg = null;

        boolean rtn = serialNoManager.isAliasExisted(SerialNo.getId(), SerialNo.getAlias());
        if (rtn) {
            throw new BusinessMessage("别名已经存在!", SysStatusCode.SERIALNO_EXSIT);
        }

        if (StringUtil.isEmpty(SerialNo.getId())) {
            serialNoManager.create(SerialNo);
            resultMsg = "添加流水号生成成功";
        } else {
            serialNoManager.update(SerialNo);
            resultMsg = "更新流水号生成成功";
        }

        return getSuccessResult(resultMsg);
    }


    /**
     * 批量删除流水号生成记录
     *
     * @param request
     * @param response
     * @throws Exception void
     */
    @RequestMapping("remove")
    @CatchErr("删除流水号失败")
    public ResultMsg<String> remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] aryIds = RequestUtil.getStringAryByStr(request, "id");

        serialNoManager.removeByIds(aryIds);
        return  getSuccessResult( "删除流水号成功");
    }

    /**
     * 取得流水号生成分页列表
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("showlist")
    public 
    PageResult showlist(HttpServletRequest request, HttpServletResponse response) throws Exception {
        QueryFilter queryFilter = getQueryFilter(request);
        Page<SerialNo> SerialNoList = (Page<SerialNo>) serialNoManager.query(queryFilter);
        return new PageResult(SerialNoList);
    }

    /**
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("preview")
    public List<SerialNo> preview(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String alias = RequestUtil.getString(request, "alias");
        List<SerialNo> identities = serialNoManager.getPreviewIden(alias);

        return identities;
    }

    /**
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getNextIdByAlias")
    public ResultMsg<String> getNextIdByAlias(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String alias = RequestUtil.getString(request, "alias");
        if (serialNoManager.isAliasExisted(null, alias)) {
            String nextId = serialNoService.genNextNo(alias);
            return getSuccessResult(nextId,"请求成功");
        }
        return getSuccessResult("","请求成功");
    }
}
