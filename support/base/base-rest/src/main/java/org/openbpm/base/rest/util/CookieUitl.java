package org.openbpm.base.rest.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * cookie操作类
 */
public class CookieUitl {
	// 自动过期
	public static final int cookie_auto_expire = -1;
	// 不过期
	public static final int cookie_no_expire = 60*60*24*365;
    /**
     * 添加cookie，cookie的生命周期为关闭浏览器即消失
     *
     * @param name
     * @param value
     * @param req
     * @param response
     */
    public static void addCookie(String name, String value) {
        addCookie(name, value, -1, RequestContext.getHttpCtx(),RequestContext.getHttpServletResponse());
    }
    
    public static void addCookie(String name, String value,int timeout) {
        addCookie(name, value, timeout, RequestContext.getHttpCtx(),RequestContext.getHttpServletResponse());
    }

    
    public static void addCookie(String name, String value, int maxAge, String path,HttpServletResponse response ){
       Cookie cookie = new Cookie(name, value);
       cookie.setPath(path);
       if (maxAge > 0) {
           cookie.setMaxAge(maxAge);
       }
       response.addCookie(cookie);
   }


    /**
     * 删除cookie
     *
     * @param name
     * @param response
     */
    public static void delCookie(String name, HttpServletRequest request, HttpServletResponse response) {
    	 Cookie uid = new Cookie(name, null);
	     uid.setPath("/");
	     uid.setMaxAge(0);
	     response.addCookie(uid);
    }
    
    
    public static String getValueByName(String cookieName,HttpServletRequest request) {
       Cookie cookies[] = request.getCookies();
       if(cookies != null)
       for (Cookie cookie : cookies) {
           if (cookie.getName().equals(cookieName)) {
               return cookie.getValue();
           }
       }
       return null;
   }

	public static String getValueByName(String name) {
		return getValueByName(name,RequestContext.getHttpServletRequest());
	}

}
