package org.openbpm.bus.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.openbpm.bus.manager.BusColumnCtrlManager;
import org.openbpm.bus.model.BusinessTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import org.openbpm.base.api.exception.BusinessError;
import org.openbpm.base.api.exception.BusinessMessage;
import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.api.query.QueryOP;
import org.openbpm.base.core.id.IdUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.db.datasource.DbContextHolder;
import org.openbpm.base.db.model.query.DefaultQueryFilter;
import org.openbpm.base.db.tableoper.TableOperator;
import org.openbpm.base.db.tableoper.TableOperatorFactory;
import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.bus.dao.BusinessTableDao;
import org.openbpm.bus.manager.BusinessColumnManager;
import org.openbpm.bus.manager.BusinessTableManager;
import org.openbpm.bus.model.BusColumnCtrl;
import org.openbpm.bus.model.BusinessColumn;
import org.openbpm.bus.util.BusinessTableCacheUtil;
import org.openbpm.sys.api.service.ISysDataSourceService;

import cn.hutool.core.collection.CollectionUtil;

/**
 * businessTable 的manager层实现类
 * 
 *
 */
@Service
public class BusinessTableManagerImpl extends BaseManager<String, BusinessTable> implements BusinessTableManager {
	@Autowired
	BusinessTableDao businessTableDao;
	@Autowired
	BusinessColumnManager businessColumnManager;
	@Autowired
    BusColumnCtrlManager busColCtrlManager;
	@Autowired
	ISysDataSourceService sysDataSourceService;
	@Resource
	JdbcTemplate jdbcTemplate;

	@Override
	public void save(BusinessTable businessTable) {
		
		if (StringUtil.isEmpty(businessTable.getId())) {
			businessTable.setId(IdUtil.getSuid());
			// 新建内部表时，表已经存在库中，则抛出异常
			if (!businessTable.isExternal() && newTableOperator(businessTable).isTableCreated()) {
				throw new BusinessMessage("表[" + businessTable.getName() + "]已经存在数据库中");
			}
			this.create(businessTable);
		} else {
			this.update(businessTable);
			busColCtrlManager.removeByTableId(businessTable.getId());// 删除关联字段控件
			businessColumnManager.removeByTableId(businessTable.getId());// 删除关联字段
		}
		
		for (BusinessColumn businessColumn : businessTable.getColumns()) {
			if (StringUtil.isEmpty(businessColumn.getId())) {
				businessColumn.setId(IdUtil.getSuid());
			}
			businessColumn.setTable(businessTable);
			businessColumn.setTableId(businessTable.getId());
			businessColumnManager.create(businessColumn);
			BusColumnCtrl ctrl = businessColumn.getCtrl();
			if (businessColumn.isPrimary()) {// 主键没控件
				continue;
			}
			if(ctrl == null) {
				throw new BusinessMessage("字段必须配置控件！");
			}
			if (StringUtil.isEmpty(ctrl.getId())) {
				ctrl.setId(IdUtil.getSuid());
			}
			ctrl.setColumnId(businessColumn.getId());
			busColCtrlManager.create(businessColumn.getCtrl());
		}
		
		if(!businessTable.isExternal()) {
			newTableOperator(businessTable).syncColumn();
		}
		
		BusinessTableCacheUtil.put(businessTable);// 入缓存
	}

	@Override
	public BusinessTable getByKey(String key) {
		QueryFilter filter = new DefaultQueryFilter();
		filter.addFilter("key_", key, QueryOP.EQUAL);
		return this.queryOne(filter);
	}

	/**
	 * <pre>
	 * 填充businessTable
	 * 把里面的columns和columns的ctrl都从数据库中获取出来
	 * </pre>
	 * 
	 * @param businessTable
	 */
	private void fill(BusinessTable businessTable) {
		if (businessTable == null) {
			return;
		}
		// 获取字段
		List<BusinessColumn> columns = businessColumnManager.getByTableId(businessTable.getId());
		for (BusinessColumn column : columns) {
			column.setCtrl(busColCtrlManager.getByColumnId(column.getId()));
			column.setTable(businessTable);
		}
		businessTable.setColumns(columns);

		TableOperator tableOperator = newTableOperator(businessTable);
		businessTable.setCreatedTable(tableOperator.isTableCreated());
	}

	@Override
	public TableOperator newTableOperator(BusinessTable businessTable) {
		JdbcTemplate dataSourceJdbcTemplate = sysDataSourceService.getJdbcTemplateByKey(businessTable.getDsKey());
		return TableOperatorFactory.newOperator(DbContextHolder.getDataSourceDbType(businessTable.getDsKey()), businessTable, dataSourceJdbcTemplate);
	}

	@Override
	public TableOperator newTableOperatorCheckExist(BusinessTable businessTable) {
		JdbcTemplate dataSourceJdbcTemplate = sysDataSourceService.getJdbcTemplateByKey(businessTable.getDsKey());
		TableOperator tableOperator = TableOperatorFactory.newOperator(DbContextHolder.getDataSourceDbType(businessTable.getDsKey()), businessTable, dataSourceJdbcTemplate);
		if (!tableOperator.isTableCreated()) {
			throw new BusinessError("实体【" + businessTable.getComment() + "】对应的表[" + businessTable.getName() + "]不存在数据库中！<br/> 请在实体中点击生成表。或者修改业务对象持久化方式为“实例表”！");
		}
		return tableOperator;
	}

	@Override
	public BusinessTable getFilledByKey(String key) {
		BusinessTable businessTable = BusinessTableCacheUtil.get(key);
		if (businessTable != null) {
			return businessTable;
		}
		businessTable = getByKey(key);
		fill(businessTable);
		BusinessTableCacheUtil.put(businessTable);// 入缓存
		return businessTable;
	}

	@Override
	public void remove(String entityId) {
		BusinessTable table = this.get(entityId);
		if (table == null)
			return;

		List<String> boNames = jdbcTemplate.queryForList("select name_ from bus_object where relation_json_ like  '%\"tableKey\":\"" + table.getKey() + "\"%'", String.class);
		if (CollectionUtil.isNotEmpty(boNames)) {
			throw new BusinessMessage("业务对象:" + boNames.toString() + "还在使用实体， 删除实体失败！");
		}

		super.remove(entityId);
	}
}
