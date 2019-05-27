package org.openbpm.bus.dao;

import org.mybatis.spring.annotation.MapperScan;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.bus.model.BusColumnCtrl;

/**
 * <pre>
 * 描述：业务字段控件表 DAO接口
 * </pre>
 */
@MapperScan
public interface BusColumnCtrlDao extends BaseDao<String, BusColumnCtrl> {

    /**
     * <pre>
     * 根据tableId删除BusColCtrl
     * </pre>
     *
     * @param tableId
     */
    void removeByTableId(String tableId);

    /**
     * <pre>
     * 根据columnId获取BusColCtrl
     * </pre>
     *
     * @param columnId
     * @return
     */
    BusColumnCtrl getByColumnId(String columnId);
}
