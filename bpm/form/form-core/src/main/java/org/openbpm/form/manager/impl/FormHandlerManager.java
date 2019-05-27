package org.openbpm.form.manager.impl;

import org.openbpm.form.manager.FormDefManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 */
@Service("bpmFormHandlerManager")
public class FormHandlerManager {


    @Resource
    FormDefManager formDefManager;

    /*	*//**
     * * field：{"NAME": "w", "SEX": "r"} table：{"TABLE1": "r", "TABLE2": "w"}
     * opinion：{"领导意见": "w", "部门意见": "r"}
     *
     * @param bpmForm
     * @param userId
     * @param actDefId
     * @param nodeId
     * @return
     *//*
	public String getPermission(String formId, String userId,
			String actDefId, String nodeId) {
		Map<String, Object> permission = null;
		if (StringUtil.isNotEmpty(userId)) {
			permission =  bpmFormRightsManager.getByFormKeyAndUserId(formId, userId, actDefId, nodeId);
		} else {
			permission = bpmFormRightsManager.getByFormId(formId);
		}

		JSONObject p = JSONObject.fromObject(permission);
		return p.toString();
	}*/

}
