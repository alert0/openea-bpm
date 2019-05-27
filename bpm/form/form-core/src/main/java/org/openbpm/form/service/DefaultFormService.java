package org.openbpm.form.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.openbpm.form.api.model.IFormDef;
import org.openbpm.form.api.service.FormService;
import org.openbpm.form.manager.FormDefManager;
import org.openbpm.form.model.FormDef;
@Service("formService")
public class DefaultFormService implements FormService {

    @Resource
    private FormDefManager formDefManager;

    public IFormDef getByFormKey(String formKey) {
        IFormDef form = formDefManager.getByKey(formKey);
        return form;
    }


    @Override
    public IFormDef getByFormId(String formId) {
        return formDefManager.get(formId);
    }


    @Override
    public String getFormExportXml(Set<String> formKeys) {
        List<String> id = new ArrayList<String>();
        for (String formKey : formKeys) {
            FormDef form =  formDefManager.getByKey(formKey);
            id.add(form.getId());
        }
        Map<String,String> map = formDefManager.exportForms(id, false);

        return map.get("form.xml");
    }


    @Override
    public void importForm(String formXmlStr) {
        try {
            formDefManager.importByFormXml(formXmlStr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导入表单失败" + e.getMessage(), e);
        }
    }

}
