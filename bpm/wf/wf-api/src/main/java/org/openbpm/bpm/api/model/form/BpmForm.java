package org.openbpm.bpm.api.model.form;

import org.openbpm.form.api.model.FormCategory;
import java.io.Serializable;

public interface BpmForm extends Serializable {
    String getFormHandler();

    String getFormHtml();

    String getFormValue();

    String getName();

    FormCategory getType();

    boolean isFormEmpty();

    void setFormHandler(String str);

    void setFormHtml(String str);

    void setFormValue(String str);

    void setName(String str);

    void setType(FormCategory formCategory);
}
