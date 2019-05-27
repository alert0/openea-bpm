package org.openbpm.base.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.openbpm.base.db.model.query.DefaultQueryFilter;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import org.openbpm.base.api.Page;
import org.openbpm.base.api.query.QueryFilter;

/**
 * 通用查询操作。
 * @param <T>
 */
@Repository
public class CommonDao<T> {
    @Resource(name="abSqlSessionTemplate")
    protected SqlSessionTemplate sqlSessionTemplate;

    private static final String NAME_SPACE = "org.openbpm.sql.common"; // mybatis命名空间

    /**
     * 执行sql语句
     *
     * @param sql void
     */
    public void execute(String sql) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sql", sql);
        String key = getNameSpace("execute");
        sqlSessionTemplate.update(key, map);
    }

    /**
     * 查询列表
     *
     * @param sql
     * @return List
     */
    public List<T> query(String sql) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sql", sql);
        String key = getNameSpace("query");
        return sqlSessionTemplate.selectList(key, map);
    }

    /**
     * 查询分页列表自行强转 com.github.pagehelper.Page<E>
     * @param sql
     * @return List
     */
    public List<T> query(String sql, Page page) {
        
        DefaultQueryFilter query = new DefaultQueryFilter();
        query.addParamsFilter("sql", sql);
        query.setPage(page);
        
        String key = getNameSpace("query");
        return   sqlSessionTemplate.selectList(key,  query);
    }

    private String getNameSpace(String sqlKey) {
        return NAME_SPACE + "." + sqlKey;
    }

  

    /**
     * 分页查询， 自行强转 com.github.pagehelper.Page<E> 获取page信息
     * @param sql
     * @param queryFilter
     * @param params
     * @return
     */
    public List<T> queryForListPage(String sql, QueryFilter queryFilter) {
        Assert.notNull(sql);
        queryFilter.addParamsFilter("sql", sql);
        return  sqlSessionTemplate.selectList(this.getNameSpace("queryFormList"), queryFilter);
    }

}
