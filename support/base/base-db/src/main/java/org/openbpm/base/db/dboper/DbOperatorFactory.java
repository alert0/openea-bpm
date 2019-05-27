package org.openbpm.base.db.dboper;

import org.openbpm.base.db.api.table.DbType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import org.openbpm.base.core.util.AppUtil;
import org.openbpm.base.db.datasource.DbContextHolder;

/**
 * <pre>
 * 描述：DbOperator的工厂类
 * </pre>
 */
public class DbOperatorFactory {
	protected static final Logger LOG = LoggerFactory.getLogger(DbOperatorFactory.class);

    private DbOperatorFactory() {

    }

    /**
     * <pre>
     * 构建一个操作器
     * </pre>
     *
     * @param type
     * @param jdbcTemplate
     * @return
     */
    public static DbOperator newOperator(String type, JdbcTemplate jdbcTemplate) {
        if (DbType.MYSQL.equalsWithKey(type)) {
            return new MysqlDbOperator(jdbcTemplate);
        }
        if (DbType.ORACLE.equalsWithKey(type)) {
            return new OracleDbOperator(jdbcTemplate);
        }
        LOG.warn("cannot get DbOperator ! DbType:{}",type);
        return null;
    }
    
    /**
     * <pre>
     * 获取本地数据库操作类
     * </pre>	
     * @return
     */
    public static DbOperator getLocal() {
    	return newOperator(DbContextHolder.getDbType(), AppUtil.getBean(JdbcTemplate.class));
    }
}
