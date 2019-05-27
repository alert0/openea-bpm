package org.openbpm.bpm.api.model.form;

import org.openbpm.base.core.util.StringUtil;
import org.openbpm.form.api.model.FormCategory;

public class DefaultForm implements BpmForm {
    private String formHandler;
    private String formHtml;
    private String formValue;
    private String name;
    private String nodeId;
    private FormCategory type;

    public String getFormHandler() {
        if (FormCategory.INNER == this.type) {
            return null;
        }
        return this.formHandler;
    }

    public void setFormHandler(String formHandler2) {
        this.formHandler = formHandler2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public FormCategory getType() {
        return this.type;
    }

    public void setType(FormCategory type2) {
        this.type = type2;
    }

    public String getFormValue() {
        return this.formValue;
    }

    public void setFormValue(String formValue2) {
        this.formValue = formValue2;
    }

    public void setId(String id) {
    }

    public String getId() {
        return "";
    }

    public boolean isFormEmpty() {
        return StringUtil.isEmpty(this.formValue);
    }

    public String getFormHtml() {
        return this.formHtml;
    }

    public void setFormHtml(String formHtml2) {
        this.formHtml = formHtml2;
    }
}
