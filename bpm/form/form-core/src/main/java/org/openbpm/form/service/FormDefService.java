package org.openbpm.form.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.openbpm.form.api.model.IFormDef;
import org.openbpm.form.api.service.IFormDefService;
import org.openbpm.form.manager.FormDefManager;

@Service
public class FormDefService implements IFormDefService {
	@Autowired
	FormDefManager formDefManager;

	@Override
	public IFormDef getByKey(String key) {
		return formDefManager.getByKey(key);
	}
}
