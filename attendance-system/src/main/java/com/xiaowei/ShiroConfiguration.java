package com.xiaowei;

import com.xiaowei.account.authorization.CustomAuthorizingRealm;
import com.xiaowei.account.authorization.RedisCacheSessionDao;
import com.xiaowei.account.authorization.TokenSessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

	private static Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

	@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
		daap.setProxyTargetClass(true);
		return daap;
	}

	@Bean
	public RedisCacheSessionDao sessionDao(){
		return new RedisCacheSessionDao();
	}


	@Bean
	public DefaultWebSessionManager sessionManager(){
		DefaultWebSessionManager defaultWebSessionManager = new TokenSessionManager();
		defaultWebSessionManager.setSessionDAO(sessionDao());
		SimpleCookie simpleCookie = new SimpleCookie();
		simpleCookie.setName("_s");
		simpleCookie.setHttpOnly(true);
		simpleCookie.setPath("/");
		simpleCookie.setMaxAge(43200);
		defaultWebSessionManager.setSessionIdCookie(simpleCookie);
		return defaultWebSessionManager;
	}

	@Bean(name = "securityManager")
	public DefaultWebSecurityManager getDefaultWebSecurityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setSessionManager(sessionManager());
		securityManager.setRealm(customAuthorizingRealm());
		return securityManager;
	}

	@Bean
	public CustomAuthorizingRealm customAuthorizingRealm(){
		CustomAuthorizingRealm customAuthorizingRealm = new CustomAuthorizingRealm();
		return new CustomAuthorizingRealm();
	}



	@Bean
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(getDefaultWebSecurityManager());
		return new AuthorizationAttributeSourceAdvisor();
	}

	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean
				.setSecurityManager(getDefaultWebSecurityManager());
		shiroFilterFactoryBean.setLoginUrl("/toLogin");
		filterChainDefinitionMap.put("/api/**", "anon");

		shiroFilterFactoryBean
				.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

}
