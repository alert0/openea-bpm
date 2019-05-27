package org.openbpm.bus.util;

import org.openbpm.bus.model.BusinessTable;
import org.springframework.jdbc.core.JdbcTemplate;

import org.openbpm.base.core.util.AppUtil;
import org.openbpm.base.db.api.table.DbType;
import org.openbpm.base.db.tableoper.MysqlTableOperator;
import org.openbpm.base.db.tableoper.TableOperator;
import org.openbpm.sys.api.service.ISysDataSourceService;

public class TableOperatorUtil {

    /**
     * 从当前可用的数据源中 加工 tableOperator
     *
     * @param dsKey
     * @param table
     * @return
     */
    public static TableOperator newOperator(BusinessTable table) {
        JdbcTemplate jdbcTemplate = AppUtil.getBean(ISysDataSourceService.class).getJdbcTemplateByKey(table.getDsKey());
        if (jdbcTemplate == null) {
            throw new RuntimeException("当前系统不存在的数据源:" + table.getDsKey());
        }

        String type = DbType.MYSQL.getKey();// DataSourceUtil 已经放了所有数据源。能不能直接从缓存中获取一下数据源的 类型

        if (DbType.MYSQL.equalsWithKey(type)) {
            return new MysqlTableOperator(table, jdbcTemplate);
        }

        throw new RuntimeException("找不到类型[" + type + "]的数据库处理者(TableOperator)");
    }
}
