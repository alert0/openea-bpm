package org.openbpm.security.login.logout;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.api.constant.BaseStatusCode;
import org.openbpm.base.api.response.impl.ResultMsg;
import org.openbpm.base.core.cache.ICache;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.base.rest.util.CookieUitl;
import org.openbpm.org.api.context.ICurrentContext;
import org.openbpm.security.jwt.service.JWTService;
import org.openbpm.security.login.UserDetailsServiceImpl;
import org.openbpm.security.login.model.LoginUser;

public class DefualtLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        JWTService jwtService = AppUtil.getBean(JWTService.class);
        ICache icache = AppUtil.getCache();

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        response.setCharacterEncoding("UTF-8");

        LoginUser user = (LoginUser) authentication.getPrincipal();
        //删除组织缓存
        icache.delByKey(ICurrentContext.CURRENT_ORG.concat(user.getId()));
        //设置过期
        if (jwtService.getJwtEnabled()) {
            //删除 UserDetial
            icache.delByKey(UserDetailsServiceImpl.loginUserCacheKey.concat(user.getAccount()));
            CookieUitl.delCookie(jwtService.getJwtHeader(), httpRequest, response);
        }

        ResultMsg resultMsg = new ResultMsg(BaseStatusCode.SUCCESS, "退出登录成功");
        response.getWriter().print(JSONObject.toJSONString(resultMsg));
        return;
    }

}
