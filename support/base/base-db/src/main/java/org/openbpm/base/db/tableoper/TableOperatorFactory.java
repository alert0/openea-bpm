package org.openbpm.base.db.tableoper;

import org.openbpm.base.db.api.table.DbType;
import org.openbpm.base.db.model.table.Table;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * TableOperator的工厂类
 *
 */
public class TableOperatorFactory {
	private TableOperatorFactory() {

	}

	/**
	 * <pre>
	 * 构建一个TableOperator
	 * </pre>
	 *
	 * @param type
	 * @param table
	 * @param jdbcTemplate
	 * @return
	 */
	public static TableOperator newOperator(String type, Table<?> table, JdbcTemplate jdbcTemplate) {
		if (DbType.MYSQL.equalsWithKey(type)) {
			return new MysqlTableOperator(table, jdbcTemplate);
		}
		if (DbType.ORACLE.equalsWithKey(type)) {
			return new OracleTableOperator(table, jdbcTemplate);
		}
		throw new RuntimeException("找不到类型[" + type + "]的数据库处理者(TableOperator)");
	}
	
}
