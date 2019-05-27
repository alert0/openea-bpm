package org.openbpm.form.manager.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.openbpm.form.manager.FormDefManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.openbpm.base.core.util.PropertyUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.form.dao.FormDefDao;
import org.openbpm.form.model.FormDef;
import org.openbpm.sys.api.model.ISysTreeNode;
import org.openbpm.sys.api.service.ISysTreeNodeService;

import cn.hutool.core.io.FileUtil;

/**
 * 表单 Manager处理实现类
 *
 */
@Service("formDefManager")
public class FormDefManagerImpl extends BaseManager<String, FormDef> implements FormDefManager {
	@Resource
	FormDefDao formDefDao;
	@Autowired
	ISysTreeNodeService sysTreeNodeService;

	@Override
	public FormDef getByKey(String key) {
		FormDef form = formDefDao.getByKey(key);
		//return Assert.notNull(form, "业务表单[" + key + "]不存在，请检查");
		return form;
	}

	@Override
	public void saveBackupHtml(FormDef formDef) {
		String formDefPath = PropertyUtil.getFormDefBackupPath();
		if (StringUtil.isEmpty(formDefPath)) {
			return;
		}

		ISysTreeNode node = sysTreeNodeService.getById(formDef.getGroupId());
		String fileName = formDefPath + File.separator + node.getKey() + File.separator + formDef.getKey() + ".html";
		FileUtil.writeUtf8String(formDef.getHtml(), fileName);
	}

	@Override
	public String getBackupHtml(FormDef formDef) {
		String formDefPath = PropertyUtil.getFormDefBackupPath();
		if (StringUtil.isNotEmpty(formDefPath)) {
			ISysTreeNode node = sysTreeNodeService.getById(formDef.getGroupId());
			String fileName = formDefPath + File.separator + node.getKey() + File.separator + formDef.getKey() + ".html";
			formDef.setHtml(FileUtil.readUtf8String(fileName));
		}

		return formDef.getHtml();
	}

	@Override
	public Map<String, String> exportForms(List<String> id, boolean b) {
		//TODO eap
		return null;
	}

	@Override
	public void importByFormXml(String formXmlStr) {
		//TODO eap

	}

	public static void main(String[] args) {
		String str = FileUtil.readUtf8String("D:\\temp\\test.html");
		System.out.println(str);
	}
}
