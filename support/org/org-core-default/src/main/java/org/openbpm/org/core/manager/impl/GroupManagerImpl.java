package org.openbpm.org.core.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.openbpm.org.core.manager.GroupManager;
import org.openbpm.org.core.manager.OrgRelationManager;
import org.openbpm.org.core.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.openbpm.base.core.id.IdUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.org.core.dao.GroupDao;
import org.openbpm.org.core.dao.UserDao;
import org.openbpm.org.core.model.OrgRelation;
import org.openbpm.org.core.model.User;

import cn.hutool.core.collection.CollectionUtil;

/**
 * <pre>
 * 描述：组织架构 处理实现类
 * </pre>
 */
@Service("groupManager")
public class GroupManagerImpl extends BaseManager<String, Group> implements GroupManager {
    @Resource
    GroupDao groupDao;
    @Resource
    UserDao userDao;
    @Autowired
    OrgRelationManager orgRelationMananger;


    public Group getByCode(String code) {
        return groupDao.getByCode(code);
    }
    
    @Override
    public Group get(String entityId) {
    	Group group =  super.get(entityId);
    	if(group != null) {
    		List<OrgRelation> orgRelationList = orgRelationMananger.getGroupPost(group.getId());
    		group.setOrgRelationList(orgRelationList);
    	}
    	return group;
    }
    @Override
    public void create(Group entity) {
    	entity.setId(IdUtil.getSuid());
    	entity.setPath(entity.getId());
    	if(StringUtil.isNotZeroEmpty(entity.getParentId())) {
    		Group parent = groupDao.get(entity.getParentId());
    		if(parent != null && parent.getPath() != null) {
    			entity.setPath(parent.getPath().concat(".").concat(entity.getId()));
    		}
    	}
    	
    	// 创建组织岗位
    	List<OrgRelation> list = entity.getOrgRelationList();
    	if(CollectionUtil.isNotEmpty(list)) {
    		list.forEach( orgRelation ->{
    			orgRelation.setGroupId(entity.getId());
    			orgRelationMananger.create(orgRelation);
    		});
    	}
    	
    	super.create(entity);
    }
    
    @Override
    public void update(Group entity) {
    	orgRelationMananger.removeGroupPostByGroupId(entity.getId());
    	
    	// 创建组织岗位
    	List<OrgRelation> list = entity.getOrgRelationList();
    	if(CollectionUtil.isNotEmpty(list)) {
    		list.forEach( orgRelation ->{
    			orgRelation.setGroupId(entity.getId());
    			orgRelationMananger.create(orgRelation);
    		});
    	}
    	super.update(entity);
    }

    public List<Group> getByUserId(String userId) {
        return groupDao.getByUserId(userId);
    }

    public List<Group> getByUserAccount(String account) {
        User user = userDao.getByAccount(account);
        return groupDao.getByUserId(user.getId());
    }

    @Override
    public Group getMainGroup(String userId) {
       List<Group> groups = groupDao.getByUserId(userId);
       if(CollectionUtil.isEmpty(groups)) return null;
       
        return groups.get(0);
    }
}
