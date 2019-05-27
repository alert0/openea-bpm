package org.openbpm.form.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.bus.api.constant.BusTableRelType;
import org.openbpm.bus.api.constant.BusinessPermissionObjType;
import org.openbpm.bus.api.model.IBusTableRel;
import org.openbpm.bus.api.model.IBusinessObject;
import org.openbpm.bus.api.model.IBusinessPermission;
import org.openbpm.bus.api.service.IBusinessDataService;
import org.openbpm.bus.api.service.IBusinessObjectService;
import org.openbpm.bus.api.service.IBusinessPermissionService;
import org.openbpm.bus.api.service.IBusinessTableService;
import org.openbpm.form.api.service.IFormDefDataService;
import org.openbpm.form.manager.FormDefManager;
import org.openbpm.form.model.FormDef;
import org.openbpm.form.model.FormDefData;

/**
 * <pre>
 * 描述：FormDefData的服务类
 * </pre>
 */
@Service
public class FormDefDataService implements IFormDefDataService {
	@Autowired
	FormDefManager formDefManager;
	@Autowired
	IBusinessObjectService businessObjectService;
	@Autowired
	IBusinessTableService businessTableService;
	@Autowired
	IBusinessPermissionService businessPermissionService;
	@Autowired
	IBusinessDataService businessDataService;

	/**
	 * <pre>
	 * 根据表单key和表单数据主表的id
	 * </pre>
	 * 
	 * @param formDefKey
	 * @param id
	 * @return
	 */
	public FormDefData getByFormDefKey(String formDefKey, String id) {
		FormDefData formDefData = new FormDefData();
		FormDef formDef = formDefManager.getByKey(formDefKey);
		formDefData.setHtml(formDef.getHtml());
		
		IBusinessPermission businessPermission = businessPermissionService.getByObjTypeAndObjVal(BusinessPermissionObjType.FORM.getKey(), formDef.getKey(), formDef.getBoKey(), true);
		
		formDefData.setPermission(businessPermission.getPermission(false));
		formDefData.setTablePermission(businessPermission.getTablePermission(false));
		
		handleInitData(formDef, formDefData);
		handleData(formDef, id, formDefData,businessPermission);

		return formDefData;
	}

	

	/**
	 * <pre>
	 * 处理boKey的初始化数据
	 * </pre>
	 * 
	 * @param formDef
	 * @param formDefData
	 */
	private void handleInitData(FormDef formDef, FormDefData formDefData) {
		if (formDefData.getInitData() == null) {
			formDefData.setInitData(new JSONObject());
		}
		JSONObject initData = formDefData.getInitData();
		IBusinessObject businessObject = businessObjectService.getFilledByKey(formDef.getBoKey());

		initData.put(formDef.getBoKey(), new JSONObject());

		for (IBusTableRel rel : businessObject.getRelation().list()) {
			initData.getJSONObject(formDef.getBoKey()).put(rel.getTableKey(), getInitData(rel));
		}
	}
	
	/**
	 * <pre>
	 * 获取初始化数据
	 * 
	 * </pre>
	 * 
	 * @param busTableRel
	 * @return
	 */
	private JSONObject getInitData(IBusTableRel busTableRel) {
		JSONObject table = new JSONObject();
		table.putAll(busTableRel.getTable().initData());
		for (IBusTableRel rel : busTableRel.getChildren()) {
			if (BusTableRelType.ONE_TO_ONE.equalsWithKey(rel.getType())) {
				// 递归处理一对一
				table.put(rel.getTableKey(), getInitData(rel));
			}
		}
		return table;
	}

	/**
	 * <pre>
	 * 处理initdata初始化数据和data数据
	 * </pre>
	 * 
	 * @param formDef
	 * @param id
	 *            bo对应数据的主键
	 * @param formDefData
	 * @param businessPermission2 
	 */
	private void handleData(FormDef formDef, String id, FormDefData formDefData, IBusinessPermission businessPermission) {
		if (formDefData.getData() == null) {
			formDefData.setData(new JSONObject());
		}

		JSONObject data = formDefData.getData();
		IBusinessObject businessObject = businessObjectService.getFilledByKey(formDef.getBoKey());
		businessObject.setPermission(businessPermission.getBusObjMap().get(formDef.getBoKey()));
		data.put(formDef.getBoKey(), businessDataService.getFormDefData(businessObject, id));
	}

}
