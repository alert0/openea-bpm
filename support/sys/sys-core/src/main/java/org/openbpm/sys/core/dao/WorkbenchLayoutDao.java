package org.openbpm.sys.core.dao;

import org.openbpm.base.dao.BaseDao;
import org.openbpm.sys.core.model.WorkbenchLayout;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface WorkbenchLayoutDao extends BaseDao<String, WorkbenchLayout> {

    void removeByUserId(String currentUserId);

    List<WorkbenchLayout> getByUserId(String userId);
}
