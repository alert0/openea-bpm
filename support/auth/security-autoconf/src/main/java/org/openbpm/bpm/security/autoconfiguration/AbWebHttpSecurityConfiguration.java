package org.openbpm.bpm.security.autoconfiguration;

import org.openbpm.org.api.context.ICurrentContext;
import org.openbpm.security.authentication.AccessDecisionManagerImpl;
import org.openbpm.security.authentication.FilterInvocationSecurityMetadataSourceImpl;
import org.openbpm.security.authentication.SecurityInterceptor;
import org.openbpm.security.filter.EncodingFilter;
import org.openbpm.security.filter.RefererCsrfFilter;
import org.openbpm.security.filter.RequestThreadFilter;
import org.openbpm.security.filter.XssFilter;
import org.openbpm.security.forbidden.DefaultAccessDeniedHandler;
import org.openbpm.security.forbidden.DefualtAuthenticationEntryPoint;
import org.openbpm.security.jwt.service.JWTService;
import org.openbpm.security.login.CustomPwdEncoder;
import org.openbpm.security.login.UserDetailsServiceImpl;
import org.openbpm.security.login.context.LoginContext;
import org.openbpm.security.login.logout.DefualtLogoutSuccessHandler;
import org.openbpm.sys.util.ContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * 鉴权配置
 *
 */
@EnableConfigurationProperties({AbSecurityProperties.class})
@Configuration
public class AbWebHttpSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AbSecurityProperties abSecurityProperties;

    @Bean
    public LoginContext loginContext() {
        return new LoginContext();
    }

    @Bean
    public ContextUtil contextUtil(ICurrentContext loginContext) {
        ContextUtil context = new ContextUtil();
        context.setCurrentContext(loginContext);
        return context;
    }

    /**
     * 允许HTML 等标签的提交的请求列表
     *
     * @return 实例
     */
    public XssFilter xssFilter() {
        XssFilter xssFilter = new XssFilter();
        List<String> ingores = new ArrayList<>();

        String ingroesConfig = abSecurityProperties.getXssIngores();
        if (StringUtils.isNotEmpty(ingroesConfig)) {
            ingores = Arrays.asList(ingroesConfig.split(","));
        }

        xssFilter.setIgnores(ingores);
        return xssFilter;
    }

    /**
     * 允许跨域的请求列表
     *
     * @return 实例
     */
    public RefererCsrfFilter csrfFilter() {
        RefererCsrfFilter filter = new RefererCsrfFilter();
        List<String> ingores = new ArrayList<>();

        String ingroesConfig = abSecurityProperties.getCsrfIngores();
        if (StringUtils.isNotEmpty(ingroesConfig)) {
            ingores = Arrays.asList(ingroesConfig.split(","));
        }

        filter.setIgnores(ingores);
        return filter;
    }

    /**
     * 退出登录反馈
     *
     * @return
     */
    public DefualtLogoutSuccessHandler logoutSuccessHandler() {
        return new DefualtLogoutSuccessHandler();
    }

    /**
     * 无权限处理器 返回resultMsg
     **/
    public DefaultAccessDeniedHandler accessDeniedHandler() {
        return new DefaultAccessDeniedHandler();
    }

    /**
     * 访问超时
     **/
    public DefualtAuthenticationEntryPoint authenticationLoginEntry() {
        return new DefualtAuthenticationEntryPoint();
    }

    /**
     * spring security 设置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().authenticationEntryPoint(new DefualtAuthenticationEntryPoint());
        http.rememberMe().key("rememberPrivateKey");
        http.logout().logoutSuccessHandler(new DefualtLogoutSuccessHandler());

        http.addFilterAt(csrfFilter(), CsrfFilter.class);
        //http.addFilter(xssFilter());

        //鉴权主入口
        SecurityInterceptor securityInterceptor = abSecurityInterceptor();
        http.addFilterBefore(securityInterceptor, FilterSecurityInterceptor.class);

        http.addFilterBefore(new RequestThreadFilter(), CsrfFilter.class);
        http.addFilterBefore(new EncodingFilter(), CsrfFilter.class);
        http.addFilterBefore(JWTAuthenticationFilter(), LogoutFilter.class);
        
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        http.headers().frameOptions().disable();
        http.csrf().disable();
    }
    

    @Bean("abJWTAuthenticationFilter")
    protected org.openbpm.security.authentication.JWTAuthenticationFilter JWTAuthenticationFilter() {
    	org.openbpm.security.authentication.JWTAuthenticationFilter abJWTAuthenticationFilter = new org.openbpm.security.authentication.JWTAuthenticationFilter();
        return abJWTAuthenticationFilter;
    }
    
    @Bean("abJWTService")
    protected JWTService abJWTService() {
    	JWTService jWTService = new JWTService();
        return jWTService;
    }
    
    /**
     * 访问决策器
     ***/
    @Bean
    protected AccessDecisionManager accessDecisionManager() {
        AccessDecisionManager decisionManager = new AccessDecisionManagerImpl();
        return decisionManager;
    }

    //获取 URL 对应的角色
    @Bean
    protected FilterInvocationSecurityMetadataSource securityMetadataSource() {
        FilterInvocationSecurityMetadataSourceImpl securityMetadataSource = new FilterInvocationSecurityMetadataSourceImpl();

        List<String> ingores = new ArrayList<>();
        String ingroesConfig = abSecurityProperties.getAuthIngores();
        if (StringUtils.isNotEmpty(ingroesConfig)) {
            ingores = Arrays.asList(ingroesConfig.split(","));
        }

        securityMetadataSource.setIgnores(ingores);
        return securityMetadataSource;
    }


    CustomPwdEncoder customPwdEncoder = new CustomPwdEncoder();

    @Bean("userDetailsService")
    public UserDetailsService userDetailsService() {
    	UserDetailsService userDetailsService = new UserDetailsServiceImpl();
        return userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //TODO will fix bug when import boot pom
        //  class file for org.springframework.security.authentication.encoding.PasswordEncoder not found
        auth.userDetailsService(userDetailsService()).passwordEncoder(customPwdEncoder);
    }

    @Bean("authenticationManager")
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager authenticationManager = super.authenticationManagerBean();
        return authenticationManager;
    }


    /**
     * 鉴权拦截器
     *
     * @return
     */
    protected SecurityInterceptor abSecurityInterceptor() {
        SecurityInterceptor intercept = new SecurityInterceptor();

//		intercept.setAuthenticationManager(authenticationManager);
        intercept.setAccessDecisionManager(new AccessDecisionManagerImpl());
        intercept.setSecurityMetadataSource(securityMetadataSource());

        return intercept;
    }
    
    @Bean("localeResolver")
    public CookieLocaleResolver cookieLocaleResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.CHINA);
        return cookieLocaleResolver;
    }

}
