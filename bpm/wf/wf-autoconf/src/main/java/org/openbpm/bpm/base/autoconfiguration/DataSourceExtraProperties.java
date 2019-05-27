package org.openbpm.bpm.base.autoconfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 *
 */
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceExtraProperties {

    /**
     * 数据源类别
     */
    private String dbType;

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}
