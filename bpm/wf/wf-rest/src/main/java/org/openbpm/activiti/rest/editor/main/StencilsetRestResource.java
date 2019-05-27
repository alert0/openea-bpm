package org.openbpm.activiti.rest.editor.main;

import com.alibaba.fastjson.JSONObject;
import org.activiti.engine.ActivitiException;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StencilsetRestResource {
    @ResponseBody
    @RequestMapping(method = {RequestMethod.GET}, value = {"/editor/stencilset"})
    public JSONObject getStencilset() {
        try {
            return JSONObject.parseObject(IOUtils.toString(getClass().getClassLoader().getResourceAsStream("stencilset.json")));
        } catch (Exception e) {
            throw new ActivitiException("Error while loading stencil set", e);
        }
    }
}
