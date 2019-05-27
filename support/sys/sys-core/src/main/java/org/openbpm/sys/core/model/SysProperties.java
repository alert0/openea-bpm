package org.openbpm.sys.core.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.openbpm.base.core.encrypt.EncryptUtil;
import org.openbpm.base.core.model.BaseModel;
import org.openbpm.sys.api.constant.EnvironmentConstant;


/**
 * <pre>
 * 描述：SYS_PROPERTIES 实体对象
 * </pre>
 */

@Data
public class SysProperties extends BaseModel implements java.io.Serializable{

    /**
     * 参数名
     */
    protected String name;

    /**
     * 别名
     */
    protected String alias;

    /**
     * 分组
     */
    protected String group;

    /**
     * 参数值
     */
    protected String value;

    /**
     * 分类使用逗号进行分割。
     */
    protected List<String> categorys = new ArrayList<String>();

    /**
     * 值是否加密存储。
     * 在编辑的时候不显示具体的值。
     */
    protected int encrypt = 0;

    /**
     * 描述。
     */
    protected String description = "";

    protected String environment = EnvironmentConstant.DEV.key();



    /**
     * 如果是加密的情况，将值进行加密。
     *
     * @throws Exception
     */
    public void setValByEncrypt() throws Exception {
        if (this.encrypt == 1) {
            this.value = EncryptUtil.encrypt(this.value);
        }
    }

    /**
     * 返回值时如果是加密情况，则将密码解密。
     *
     * @return
     */
    public String getRealVal() {
        if (this.encrypt == 1) {
            try{
                return EncryptUtil.decrypt(this.value);
            }catch (Throwable t){
                //TODO warning
                return this.value;
            }
        }
        return this.value;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("name", this.name)
                .append("alias", this.alias)
                .append("group", this.group)
                .append("value", this.value)
                .append("createTime", this.createTime)
                .toString();
    }
}