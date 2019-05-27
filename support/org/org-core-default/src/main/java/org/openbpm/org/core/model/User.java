package org.openbpm.org.core.model;

import java.util.List;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.openbpm.base.core.model.BaseModel;
import org.openbpm.org.api.model.IUser;


/**
 * <pre>
 * 描述：用户表 实体对象
 * </pre>
 */
@Data
public class User extends BaseModel implements IUser{
    /**
     * 姓名
     */
    protected String fullname;

    /**
     * 账号
     */
    protected String account;

    /**
     * 密码
     */
    protected String password;

    /**
     * 邮箱
     */
    protected String email;

    /**
     * 手机号码
     */
    protected String mobile;

    /**
     * 微信号
     */
    protected String weixin;

    /**
     * 创建时间
     */
    protected java.util.Date createTime;

    /**
     * 地址
     */
    protected String address;

    /**
     * 头像
     */
    protected String photo;

    /**
     * 性别：男，女，未知
     */
    protected String sex;

    /**
     * 来源
     */
    protected String from = "system";

    /**
     * 0:禁用，1正常
     */
    protected Integer status;


    /**
     * 组织ID，用于在组织下添加用户。
     */
    protected String groupId = "";
    
    /**
     * 用户关系
     */
    protected List<OrgRelation> orgRelationList; 
    



    public String getUserId() {
        return this.id;
    }

    public void setUserId(String userId) {
        this.id = userId;

    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("fullname", this.fullname)
                .append("account", this.account)
                .append("password", this.password)
                .append("email", this.email)
                .append("mobile", this.mobile)
                .append("weixin", this.weixin)
                .append("createTime", this.createTime)
                .append("address", this.address)
                .append("photo", this.photo)
                .append("sex", this.sex)
                .append("from", this.from)
                .append("status", this.status)
                .toString();
    }

}