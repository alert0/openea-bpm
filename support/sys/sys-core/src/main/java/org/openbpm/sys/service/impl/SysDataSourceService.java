package org.openbpm.sys.service.impl;

import org.openbpm.sys.api.model.ISysDataSource;
import org.openbpm.sys.api.service.ISysDataSourceService;
import org.openbpm.sys.core.manager.SysDataSourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * <pre>
 * 描述：系统数据源对其他模块的服务类
 * </pre>
 */
@Service
public class SysDataSourceService implements ISysDataSourceService {
    @Autowired
    SysDataSourceManager sysDataSourceManager;

    @Override
    public ISysDataSource getByKey(String key) {
        return sysDataSourceManager.getByKey(key);
    }

    @Override
    public DataSource getDataSourceByKey(String key) {
        return sysDataSourceManager.getDataSourceByKey(key);
    }

    @Override
    public JdbcTemplate getJdbcTemplateByKey(String key) {
        return sysDataSourceManager.getJdbcTemplateByKey(key);
    }
}
