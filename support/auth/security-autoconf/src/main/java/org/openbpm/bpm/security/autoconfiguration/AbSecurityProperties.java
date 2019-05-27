package org.openbpm.bpm.security.autoconfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 *
 */
@ConfigurationProperties(prefix = "ab.security")
public class AbSecurityProperties {

	/**
	 * 逗号分隔
	*/
	
	/**忽略xss 的地址 **/
	private String xssIngores = "";
	/**忽略跨域访问 的地址 **/
	private String csrfIngores = "127.0.0.1";
	/**忽略鉴权 的地址 **/
	private String authIngores = "/login.*";
	
	
	public String getXssIngores() {
		return xssIngores;
	}
	public void setXssIngores(String xssIngores) {
		this.xssIngores = xssIngores;
	}
	public String getCsrfIngores() {
		return csrfIngores;
	}
	public void setCsrfIngores(String csrfIngores) {
		this.csrfIngores = csrfIngores;
	}
	public String getAuthIngores() {
		return authIngores;
	}
	public void setAuthIngores(String authIngores) {
		this.authIngores = authIngores;
	}
	 
	
	
}
