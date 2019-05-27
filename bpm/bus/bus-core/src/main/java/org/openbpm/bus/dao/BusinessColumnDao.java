package org.openbpm.bus.dao;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.bus.model.BusinessColumn;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;

/**
 * <pre>
 * 描述：业务字段表 DAO接口
 * </pre>
 */
@MapperScan
public interface BusinessColumnDao extends BaseDao<String, BusinessColumn> {

    /**
     * <pre>
     * 根据tableId删除字段
     * </pre>
     *
     * @param tableId
     */
    void removeByTableId(String tableId);

    /**
     * <pre>
     * 根据tableId获取字段
     * </pre>
     *
     * @param tableId
     * @return
     */
    List<BusinessColumn> getByTableId(String tableId);
}
