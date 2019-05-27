package org.openea.bpm.adaptor.model;

import lombok.Data;
import org.openbpm.org.api.model.IUser;

@Data
public class EapUser implements IUser {


    protected String userId;
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
     * 0:禁用，1正常
     */
    protected Integer status;

}
