package org.openbpm.bpm.rest.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ZipUtil;
import org.openbpm.base.api.aop.annotion.CatchErr;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.core.util.ThreadMsgUtil;
import org.openbpm.base.rest.GenericController;
import org.openbpm.base.rest.util.RequestUtil;
import org.openbpm.bpm.core.manager.BpmDefOverallManager;
import org.openbpm.bpm.core.model.overallview.BpmOverallView;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RequestMapping({"/bpm/overallView"})
@RestController
public class BpmOverallViewController extends GenericController {
    private static final String ROOT_PATH = ("temp" + File.separator + "tempZip");
    @Resource
    BpmDefOverallManager bpmDefOverallManager;

    @RequestMapping({"getOverallView"})
    public ResultMsg<BpmOverallView> getOverallView(@RequestParam String defId) throws Exception {
        return getSuccessResult(this.bpmDefOverallManager.getBpmOverallView(defId));
    }

    @RequestMapping({"overallViewSave"})
    @CatchErr
    public ResultMsg overallViewSave(@RequestBody BpmOverallView overAllView) throws Exception {
        this.bpmDefOverallManager.saveBpmOverallView(overAllView);
        return getSuccessResult("保存成功！");
    }

    @RequestMapping({"exportBpmDefinition"})
    @CatchErr("下载失败")
    public void exportXml(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] defIds = RequestUtil.getStringAryByStr(request, "defIds");
        if (!ArrayUtil.isEmpty(defIds)) {
            downLoadFile(request, response, this.bpmDefOverallManager.exportBpmDefinitions(defIds), "agileBpmDefs" + DateUtil.format(new Date(), "yyyy_MMdd_HHmm"));
        }
    }

    private static String getWebContextPath() {
        String path = ClassUtil.getClassPath();
        //TODO will fix for jetty:run
        if(path.lastIndexOf("WEB-INF")>0){
            return StringUtil.trimSuffix(path.substring(0, path.lastIndexOf("WEB-INF")), "/");
        }else{
            return StringUtil.trimSuffix(path.substring(0, path.lastIndexOf("classes")), "/");
        }

    }

    private static void downLoadFile(HttpServletRequest request, HttpServletResponse response, Map<String, String> fileContentMap, String zipName) throws Exception {
        String zipPath = getWebContextPath() + ROOT_PATH + File.separator + zipName;
        for (Entry<String, String> ent : fileContentMap.entrySet()) {
            FileUtil.writeUtf8String((String) ent.getValue(), zipPath + File.separator + ((String) ent.getKey()));
        }
        File file = ZipUtil.zip(zipPath);
        FileUtil.del(zipPath);
        RequestUtil.downLoadFile(response, file);
        FileUtil.del(file);
    }

    @RequestMapping({"importPreview"})
    @CatchErr
    public ResultMsg<Map<String, List<BpmOverallView>>> importPreview(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
        MultipartFile fileLoad = request.getFile("xmlFile");
        String name = fileLoad.getOriginalFilename();
        String unZipFilePath = getWebContextPath() + File.separator + "temp" + File.separator + "unzip";
        try {
            String fileDir = StringUtils.substringBeforeLast(name, ".");
            File file = FileUtil.mkdir(unZipFilePath + File.separator + fileLoad.getOriginalFilename());
            fileLoad.transferTo(file);
            ZipUtil.unzip(file);
            unZipFilePath = unZipFilePath + File.separator + fileDir;
            String flowXml = FileUtil.readUtf8String(unZipFilePath + "/agilebpm-flow.xml");
            if (StringUtil.isEmpty(flowXml)) {
                throw new BusinessException("导入的文件缺少 流程信息 agilebpm-flow.xml");
            }
            checkXmlFormat(flowXml);
            ResultMsg<Map<String, List<BpmOverallView>>> successResult = getSuccessResult(this.bpmDefOverallManager.importPreview(flowXml));
            File unzipfile = new File(unZipFilePath);
            if (unzipfile.exists()) {
                FileUtil.del(unzipfile);
            }
            return successResult;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } catch (Throwable th) {
            File unzipfile2 = new File(unZipFilePath);
            if (unzipfile2.exists()) {
                FileUtil.del(unzipfile2);
            }
            throw th;
        }
    }

    @RequestMapping({"importSave"})
    @CatchErr
    public ResultMsg<String> importSave(@RequestBody List<BpmOverallView> overAllView) throws Exception {
        if (CollectionUtil.isEmpty(overAllView)) {
            throw new RuntimeException("导入的数据不能为空！");
        }
        this.bpmDefOverallManager.importSave(overAllView);
        return getSuccessResult("导入成功!<br/>" + ThreadMsgUtil.getMessage(true, "<br/>"));
    }

    private void checkXmlFormat(String xml) throws Exception {
        String nextName = "agilebpmXml";
        Element root = DocumentHelper.parseText(xml).getRootElement();
        String msg = "导入文件格式不对";
        if (!root.getName().equals("agilebpmXmlList")) {
            throw new Exception(msg);
        }
        for (Object elm : root.elements()) {
            if (!((Element)elm).getName().equals(nextName)) {
                throw new Exception(msg);
            }
        }
    }
}
