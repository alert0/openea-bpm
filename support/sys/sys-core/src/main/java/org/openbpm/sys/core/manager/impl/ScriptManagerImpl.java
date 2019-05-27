package org.openbpm.sys.core.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.openbpm.sys.core.dao.ScriptDao;
import org.openbpm.sys.core.manager.ScriptManager;
import org.openbpm.sys.core.model.Script;
import org.springframework.stereotype.Service;

import org.openbpm.base.manager.impl.BaseManager;


@Service("scriptManager")
public class ScriptManagerImpl extends BaseManager<String, Script> implements ScriptManager {
    @Resource
    private ScriptDao scriptDao;

    @Override
    public List<String> getDistinctCategory() {
        return scriptDao.getDistinctCategory();
    }

    @Override
    public Integer isNameExists(String name) {
        return scriptDao.isNameExists(name);
    }

}
