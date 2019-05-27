package org.openbpm.bus.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import org.openbpm.base.core.model.BaseModel;
import org.openbpm.base.core.util.JsonUtil;
import org.openbpm.bus.api.constant.RightsType;
import org.openbpm.bus.api.model.IBusinessObject;
import org.openbpm.bus.api.model.permission.IBusObjPermission;
import org.openbpm.bus.model.permission.BusColumnPermission;
import org.openbpm.bus.model.permission.BusObjPermission;
import org.openbpm.bus.model.permission.BusTablePermission;

/**
 * <pre>
 * 业务对象
 * 用来描述 多个BusinessTable的关系
 * ps:规定每个BusinessTable在一个业务对象中只能使用一次
 * 假如真的有业务需要一个业务对象用重复的表，可以定义两个业务表
 * 因为可以利用外部表的方式操作同一张表
 * </pre>
 *
 *
 */
@Data
public class BusinessObject extends BaseModel implements IBusinessObject {
	/**
	 * key
	 */
	@NotEmpty
	private String key;
	/**
	 * 名字
	 */
	@NotEmpty
	private String name;
	/**
	 * 描述
	 */
	private String desc;
	/**
	 * relation字段用来持久化入库的字符串字段
	 */
	@NotEmpty
	private String relationJson;
	/**
	 * 分组id
	 */
	private String groupId;
	/**
	 * 分组名称
	 */
	private String groupName;
	/**
	 * 持久化类型 枚举： BusinessObjectPersistenceType
	 */
	@NotEmpty
	private String persistenceType;

	// 以下的字段不会进行持久化（不存数据库）
	private BusTableRel relation;
	/**
	 * bo权限
	 */
	private BusObjPermission permission;


	public void setRelationJson(String relationJson) {
		this.relationJson = relationJson;
		this.relation = JsonUtil.parseObject(relationJson, BusTableRel.class);
	}


	public void setRelation(BusTableRel relation) {
		this.relation = relation;
		this.relationJson = JsonUtil.toJSONString(relation);
	}

	@Override
	public void setPermission(IBusObjPermission permission) {
		this.permission = (BusObjPermission) permission;
	}

	@Override
	public boolean haveTableDbEditRights(String tableKey) {
		return haveTableDbRights(true, tableKey);
	}

	@Override
	public boolean haveTableDbReadRights(String tableKey) {
		return haveTableDbRights(false, tableKey);
	}

	@Override
	public boolean haveColumnDbEditRights(String tableKey, String columnKey) {
		return haveColumnDbRights(true, tableKey, columnKey);
	}

	@Override
	public boolean haveColumnDbReadRights(String tableKey, String columnKey) {
		return haveColumnDbRights(false, tableKey, columnKey);
	}

	/**
	 * <pre>
	 * 判断某个字段有没有数据库某权限
	 * </pre>
	 * 
	 * @param isEdit
	 *            true:是否有操作权限；false:是否有读取权限
	 * @param tableKey
	 * @param columnKey
	 * 
	 * @return
	 */
	private boolean haveColumnDbRights(boolean isEdit, String tableKey, String columnKey) {

		RightsType rightsType = null;
		if (this.permission != null) {
			BusTablePermission tablePermission = this.permission.getTableMap().get(tableKey);// 获取表权限
			if (tablePermission != null) {
				BusColumnPermission columnPermission = tablePermission.getColumnMap().get(columnKey);// 获取字段权限
				if (columnPermission != null) {
					rightsType = RightsType.getByKey(columnPermission.getResult());
				}
			}
		}
		if (rightsType == null) {
			rightsType = RightsType.getDefalut();
		}
		if (isEdit) {
			return rightsType.isDbEditable();
		} else {
			return rightsType.isDbReadable();
		}
	}

	/**
	 * <pre>
	 * 判断某个表有没有数据库某权限
	 * </pre>
	 * 
	 * @param isEdit
	 *            true:是否有操作权限；false:是否有读取权限
	 * @param tableKey
	 * 
	 * @return
	 */
	private boolean haveTableDbRights(boolean isEdit, String tableKey) {
		if (this.permission != null) {
			BusTablePermission tablePermission = this.permission.getTableMap().get(tableKey);// 获取表权限
			if (tablePermission != null) {
				for (BusinessColumn column : this.relation.find(tableKey).getTable().getColumnsWithoutPk()) {
					if (isEdit && haveColumnDbEditRights(tableKey, column.getKey())) {// 有一个字段有编辑权限则结束了
						return true;
					}
					if (!isEdit && haveColumnDbReadRights(tableKey, column.getKey())) {// 有一个字段有读取权限则结束了
						return true;
					}
				}
				return false;// 字段没权限，那表才算没有权限
			}
		}
		if (isEdit) {
			return RightsType.getDefalut().isDbEditable();
		} else {
			return RightsType.getDefalut().isDbReadable();
		}
	}
	
	@Override
	public Set<String> calDataSourceKeys() {
		Set<String> keys = new HashSet<>();
		for(BusTableRel rel:relation.list()) {
			keys.add(rel.getTable().getDsKey());
		}
		return keys;
	}
}
