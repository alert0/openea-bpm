package org.openbpm.bus.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import org.openbpm.base.api.model.IBaseModel;
import org.openbpm.base.db.model.table.Table;
import org.openbpm.bus.api.constant.BusColumnCtrlType;
import org.openbpm.bus.api.model.IBusinessColumn;
import org.openbpm.bus.api.model.IBusinessTable;

/**
 * 业务表
 * 
 *
 */
@Data
public class BusinessTable extends Table<BusinessColumn> implements IBaseModel, IBusinessTable {
	/**
	 * 主键
	 */
	@NotEmpty
	private String id;
	/**
	 * 业务表的别名
	 */
	@NotEmpty
	private String key;
	/**
	 * 数据源别名
	 */
	@NotEmpty
	private String dsKey;
	/**
	 * 数据源名字
	 */
	@NotEmpty
	private String dsName;
	/**
	 * 分组id
	 */
	private String groupId;
	/**
	 * 分组名称
	 */
	private String groupName;
	/**
	 * <pre>
	 * 是否外部表
	 * 外部表是以数据库表生成的businessTable数据
	 * 内部表是以businessTable生成的数据库表
	 * </pre>
	 */
	private boolean external;

	// 以下字段不存数据库
	/**
	 * 是否在数据库创建了表
	 */
	private boolean createdTable;
	
    // 创建时间
    protected Date createTime;
    // 创建人ID
    protected String createBy;
    // 更新时间
    protected Date updateTime;
    // 更新人ID
    protected String updateBy;
    


	/**
	 * <pre>
	 * 获取主键Name
	 * </pre>
	 * 
	 * @return
	 */
	public String getPkName() {
		if (this.getPkColumn() == null) {
			return "";
		}
		return this.getPkColumn().getName();
	}

	/**
	 * <pre>
	 * 获取主键Key
	 * </pre>
	 * 
	 * @return
	 */
	public String getPkKey() {
		if (this.getPkColumn() == null) {
			return "";
		}
		return this.getPkColumn().getKey();
	}

	/**
	 * <pre>
	 * 直接调用父类
	 * 本来这个类是没啥用的，但是json解析时，它获取不了泛型C，所以我在这里告诉泛型的具体实现
	 * </pre>
	 */
	@Override
	public void setColumns(List<BusinessColumn> columns) {
		super.setColumns(columns);
	}

	/**
	 * <pre>
	 * 直接调用父类
	 * 本来这个类是没啥用的，但是json解析时，它获取不了泛型C，所以我在这里告诉泛型的具体实现
	 * </pre>
	 */
	@Override
	public List<BusinessColumn> getColumns() {
		return super.getColumns();
	}

	public boolean isCreatedTable() {
		return createdTable;
	}

	public void setCreatedTable(boolean createdTable) {
		this.createdTable = createdTable;
	}

	@Override
	public List<BusinessColumn> getColumnsWithoutPk() {
		if (columns == null) {
			return null;
		}
		List<BusinessColumn> columnList = new ArrayList<>();
		for (BusinessColumn column : columns) {
			if (!column.isPrimary()) {
				columnList.add(column);
			}
		}
		return columnList;
	}

	public List<BusinessColumn> getColumnsWithOutHidden() {
		if (columns == null) {
			return null;
		}
		List<BusinessColumn> columnList = new ArrayList<>();
		for (BusinessColumn column : columns) {
			if (column.isPrimary())
				continue;

			if (column.getCtrl() == null || BusColumnCtrlType.HIDDEN.getKey().equals(column.getCtrl().getType())) {
				continue;
			}

			columnList.add(column);
		}

		return columnList;
	}

	@Override
	public Map<String, Object> initDbData() {
		Map<String, Object> map = new HashMap<>();
		for (IBusinessColumn column : this.getColumnsWithoutPk()) {
			map.put(column.getName(), column.initValue());
		}
		return map;
	}

	@Override
	public Map<String, Object> initData() {
		Map<String, Object> map = new HashMap<>();
		for (IBusinessColumn column : this.getColumnsWithoutPk()) {
			map.put(column.getKey(), column.initValue());
		}
		return map;
	}

	@Override
	public BusinessColumn getColumnByKey(String key) {
		for (BusinessColumn column : this.columns) {
			if (key.equals(column.getKey())) {
				return column;
			}
		}
		return null;
	}

	 @Override
	    public Date getCreateTime() {
	        return createTime;
	    }

	    @Override
	    public void setCreateTime(Date createTime) {
	        this.createTime = createTime;
	    }

	    @Override
	    public String getCreateBy() {
	        return createBy;
	    }

	    @Override
	    public void setCreateBy(String createBy) {
	        this.createBy = createBy;
	    }
	    @Override
	    public Date getUpdateTime() {
	        return updateTime;
	    }

	    @Override
	    public void setUpdateTime(Date updateTime) {
	        this.updateTime = updateTime;
	    }

	    @Override
	    public String getUpdateBy() {
	        return updateBy;
	    }

	    @Override
	    public void setUpdateBy(String updateBy) {
	        this.updateBy = updateBy;
	    }
}
