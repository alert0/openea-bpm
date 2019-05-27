package org.openbpm.bus.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.openbpm.bus.manager.BusinessObjectManager;
import org.openbpm.bus.manager.BusinessTableManager;
import org.openbpm.bus.model.BusinessObject;
import org.openbpm.bus.model.BusinessTable;
import org.openbpm.bus.service.BusinessPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.api.exception.BusinessMessage;
import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.api.query.QueryOP;
import org.openbpm.base.db.model.query.DefaultQueryFilter;
import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.bus.api.constant.BusTableRelType;
import org.openbpm.bus.dao.BusinessObjectDao;
import org.openbpm.bus.model.BusTableRel;
import org.openbpm.bus.model.BusinessColumn;

import cn.hutool.core.collection.CollectionUtil;

/**
 * BusinessObject 的manager层实现类
 * 
 *
 */
@Service
public class BusinessObjectManagerImpl extends BaseManager<String, BusinessObject> implements BusinessObjectManager {
	@Resource
	BusinessObjectDao businessObjectDao;
	@Autowired
	BusinessTableManager businessTableManager;
	@Autowired
	BusinessPermissionService businessPermissionService;
	@Resource
	JdbcTemplate jdbcTemplate;
	
	@Override
	public BusinessObject getByKey(String key) {
		QueryFilter filter = new DefaultQueryFilter();
		filter.addFilter("key_", key, QueryOP.EQUAL);
		return this.queryOne(filter);
	}

	@Override
	public BusinessObject getFilledByKey(String key) {
		BusinessObject businessObject = getByKey(key);
		fill(businessObject);
		return businessObject;
	}

	/**
	 * <pre>
	 * 填充businessObject的数据
	 * 将其rel中的table数据都设置进去
	 * table中的ctrl也被填充好
	 * </pre>
	 * 
	 * @param businessObject
	 * @param loadPermission
	 *            是否加载权限
	 */
	private void fill(BusinessObject businessObject) {
		if (businessObject == null) {
			return;
		}

		for (BusTableRel rel : businessObject.getRelation().list()) {
			rel.setTable(businessTableManager.getFilledByKey(rel.getTableKey()));
			rel.setBusObj(businessObject);
		}

		handleSetParentRel(businessObject.getRelation());
	}

	/**
	 * <pre>
	 * 处理设置父节点
	 * </pre>
	 * 
	 * @param rel
	 */
	private void handleSetParentRel(BusTableRel rel) {
		for (BusTableRel r : rel.getChildren()) {
			r.setParent(rel);
			handleSetParentRel(r);// 递归子节点
		}
	}

	@Override
	public List<JSONObject> boTreeData(String key) {
		BusinessObject businessObject = getByKey(key);
		BusTableRel busTableRel = businessObject.getRelation();
		List<JSONObject> list = new ArrayList<>();
		hanldeBusTableRel(busTableRel, "0", list);

		if (CollectionUtil.isNotEmpty(list)) {
			list.get(0).put("alias", key);
		}
		return list;
	}

	/**
	 * <pre>
	 * 递归构建boTree
	 * </pre>
	 * 
	 * @param busTableRel
	 * @param parentId
	 * @param list
	 */
	private void hanldeBusTableRel(BusTableRel busTableRel, String parentId, List<JSONObject> list) {
		BusinessTable businessTable = businessTableManager.getFilledByKey(busTableRel.getTableKey());
		JSONObject root = new JSONObject();
		root.put("id", businessTable.getId());
		root.put("key", businessTable.getKey());
		root.put("name", businessTable.getName() + "(" + BusTableRelType.getByKey(busTableRel.getType()).getDesc() + ")");
		root.put("comment",businessTable.getComment());
		root.put("parentId", parentId);
		root.put("nodeType", "table");// 节点类型-表
		list.add(root);

		for (BusinessColumn businessColumn : businessTable.getColumns()) {
			JSONObject columnJson = new JSONObject();
			columnJson.put("id", businessColumn.getId());
			columnJson.put("key", businessColumn.getKey());
			columnJson.put("name", businessColumn.getComment());
			columnJson.put("tableKey", businessTable.getKey());
			columnJson.put("parentId", businessTable.getId());
			columnJson.put("nodeType", "column");// 节点类型-字段
			list.add(columnJson);
		}
		for (BusTableRel rel : busTableRel.getChildren()) {
			hanldeBusTableRel(rel, businessTable.getId(), list);
		}
	}
	
	
	/**
	 *  form 
	 * def 
	 */
	@Override
	public void remove(String entityId) {
		BusinessObject businessObject = this.get(entityId);
		if(businessObject == null) return;
		
		List<String> names = jdbcTemplate.queryForList(" select name_ from form_def where bo_key_ = '"+businessObject.getKey()+"'", String.class);
		if(CollectionUtil.isNotEmpty(names)) {
			throw new BusinessMessage("表单:"+names.toString()+"还在使用业务对象， 删除业务对象失败！"); 
		}
		
		super.remove(entityId);
	}
}
