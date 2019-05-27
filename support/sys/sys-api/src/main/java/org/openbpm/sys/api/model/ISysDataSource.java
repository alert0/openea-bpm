package org.openbpm.sys.api.model;


/**
 * <pre>
 * 描述：系统数据源
 * </pre>
 */
public interface ISysDataSource {
    /**
     * <pre>
     * 数据源的别名
     * </pre>
     *
     * @return
     */
    public String getKey();

    /**
     * <pre>
     * 名字
     * </pre>
     *
     * @return
     */
    public String getName();

    /**
     * <pre>
     * 描述
     * </pre>
     *
     * @return
     */
    public String getDesc();

    /**
     * <pre>
     * 数据库类型 枚举在org.openbpm.base.api.db.DbType 的key
     * </pre>
     *
     * @return
     */
    public String getDbType();

}
