package org.openbpm.org.core.manager.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.openbpm.org.core.manager.OrgRelationManager;
import org.openbpm.org.core.manager.UserManager;
import org.springframework.stereotype.Service;

import org.openbpm.base.core.encrypt.EncryptUtil;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.manager.impl.BaseManager;
import org.openbpm.org.core.constant.RelationTypeConstant;
import org.openbpm.org.core.dao.UserDao;
import org.openbpm.org.core.model.OrgRelation;
import org.openbpm.org.core.model.User;

import cn.hutool.core.collection.CollectionUtil;

/**
 * <pre>
 * 描述：用户表 处理实现类
 * </pre>
 */
@Service("userManager")
public class UserManagerImpl extends BaseManager<String, User> implements UserManager {
    @Resource
    UserDao userDao;
    @Resource
    OrgRelationManager orgRelationMananger;

    public User getByAccount(String account) {
        return this.userDao.getByAccount(account);
    }

    @Override
    public boolean isUserExist(User user) {
        return userDao.isUserExist(user)>0;
    }

	@Override
	public List<User> getUserListByRelation(String relId, String type) {
		if(type.equals(RelationTypeConstant.POST_USER.getKey())) {
			OrgRelation postRelation = orgRelationMananger.get(relId);
			if(postRelation == null) return Collections.emptyList();
			return userDao.getUserListByPost(postRelation.getGroupId(),postRelation.getUserId());
		}
		
		return userDao.getUserListByRelation(relId,type);
	}
	
	/**
	 * 通过ID 获取会带上用户关系信息
	 */
	@Override
	public User get(String entityId) {
		
		User user =  super.get(entityId);
		if(user!= null) {
			user.setOrgRelationList(orgRelationMananger.getUserRelation(entityId, null));
		}
		return user;
	}
	
	@Override
	public void remove(String entityId) {
		orgRelationMananger.removeByUserId(entityId);
		super.remove(entityId);
	}

	@Override
	public void saveUserInfo(User user) {
		if(StringUtil.isEmpty(user.getId())) {
			if(StringUtil.isEmpty(user.getPassword())) {
				user.setPassword("1");
			}
            user.setPassword(EncryptUtil.encryptSha256(user.getPassword()));
			this.create(user);
		}else {
			this.update(user);
			orgRelationMananger.removeByUserId(user.getId());
		}
		
		List<OrgRelation> orgRelationList = user.getOrgRelationList();
		if(CollectionUtil.isEmpty(orgRelationList)) return;
		
		orgRelationList.forEach( rel ->{
			rel.setUserId(user.getId());
			orgRelationMananger.create(rel);
		});
		 
	}

}
