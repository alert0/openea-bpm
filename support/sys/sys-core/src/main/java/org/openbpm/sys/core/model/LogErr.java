package org.openbpm.sys.core.model;

import java.util.Date;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.openbpm.base.api.model.IDModel;


/**
 *  错误日志 实体对象
 *  @author Jeff
 */

@Data
public class LogErr implements IDModel {
	private static final long serialVersionUID = 1L;
	protected String id; /* 主键 */
    protected String account; /* 登录帐号 */
    protected String ip; /* IP地址 */
    protected String ipAddress; /* IP地址 */
	protected String url; /* URL地址 */
    protected String content; /* 内容 */
    protected String stackTrace;/*堆栈*/
    protected String status = "unchecked";//状态   checked fixed
    protected String requestParam;
    protected java.util.Date createTime; /* 创建时间 */

    public LogErr() {
        super();
    }

    public LogErr(String id, String account, String ip, String url,
                  String content, Date createTime) {
        super();
        this.id = id;
        this.account = account;
        this.ip = ip;
        this.url = url;
        this.content = content;
        this.createTime = createTime;
    }

	/**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this).append("id", this.id)
                .append("account", this.account).append("ip", this.ip)
                .append("url", this.url).append("content", this.content)
                .append("createTime", this.createTime).toString();
    }
}