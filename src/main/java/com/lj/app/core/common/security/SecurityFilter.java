package com.lj.app.core.common.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lj.app.core.common.base.entity.UpmUser;
import com.lj.app.core.common.properties.PropertiesUtil;
import com.lj.app.core.common.util.SessionCode;
import com.lj.app.core.common.util.StringUtil;

public class SecurityFilter implements Filter {
	
	private static Log logger =  LogFactory.getLog(SecurityFilter.class);

	private String isNeedAuthen;
	
	private Set<String> skipValidatePageSet = new HashSet<String>();
	
	private Set<String> skipValidateDirSet = new HashSet<String>();

	private Set<String> skipSessionValidateDirSet = new HashSet<String>();
	
	private Set<String> needValidateUrlSet;//uap_permission中需要验证的url集合
	
	private Set<String> disabledAccessUrlSet;//uap_permission中不可访问的url集合
	
	public void init(FilterConfig filterConfig) {
		isNeedAuthen = filterConfig.getInitParameter("isNeedAuthen");
		String contextPath = filterConfig.getInitParameter("contextPath");
		
		String skipPrivilegeValidatePage = filterConfig.getInitParameter("skipPrivilegeValidatePage");
		
		if(StringUtil.isNotBlank(skipPrivilegeValidatePage)) {
			String[] urls = skipPrivilegeValidatePage.split(",");
			for (int i = 0; i < urls.length; i++) {
				skipValidatePageSet.add(contextPath + urls[i].trim());
			}
		}
		String skipPrivilegeValidateDir = filterConfig.getInitParameter("skipPrivilegeValidateDir");
		if(StringUtil.isNotBlank(skipPrivilegeValidateDir)) {
			String[] dirs = skipPrivilegeValidateDir.split(",");
			for (int i = 0; i < dirs.length; i++) {
				skipValidateDirSet.add(contextPath + dirs[i].trim());
			}
		}
		String skipSessionValidate = filterConfig.getInitParameter("skipSessionValidate");
		if(StringUtil.isNotBlank(skipSessionValidate)) {
			String[] pages = skipSessionValidate.split(",");
			for (int i = 0; i < pages.length; i++) {
				skipSessionValidateDirSet.add(contextPath + pages[i].trim());
			}
		}
		
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain) {
		try {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			HttpServletResponse response = (HttpServletResponse) servletResponse;
			HttpSession session = request.getSession();
			UpmUser  upmUser = (UpmUser)session.getAttribute(SessionCode.MAIN_ACCT);
			
			String requestUri = request.getRequestURI().toString();
			String contextPath = request.getContextPath();
			CmSecurityContext securityContext = (CmSecurityContext) session.getAttribute(SecurityConstants.SECURITY_CONTEXT);
			
			String multiLogin = PropertiesUtil.getPropertyTrim("multiLogin");
			String isNotAllowDiffClientLogin = PropertiesUtil.getPropertyTrim("isNotAllowDiffClientLogin");
			
			if(upmUser != null && session != null){
				if(multiLogin != null && multiLogin.equals("N") && isNotAllowDiffClientLogin != null 
						&& isNotAllowDiffClientLogin.equalsIgnoreCase("N")){
					String loginNo = upmUser.getLoginNo();
					String newClientIp= "";
				}
			}
			
			//如果用户已经登录，那么就直接到首页，防止重复登录
			if(requestUri.equals(contextPath + "/login.jsp")) {
				if(securityContext != null) {
					response.sendRedirect(contextPath+ "/index.jsp");
					return;
				}
			}
			
			//session验证
			boolean sessionValidate = validateSession(securityContext, requestUri, contextPath);
			if(!sessionValidate) {
				response.sendRedirect(contextPath + "/jsp/common/sessionexpire.jsp");
				return;
			}
			
			//权限验证
			if(isNeedAuthen.equals("true")) {
				boolean permissionValidate = validatePermission(securityContext, requestUri, contextPath);
				
				if(!permissionValidate) {
					response.sendRedirect(contextPath + "/jsp/common/nopermission.jsp");
					return ;
				}
			}
			
			String queryStr = request.getQueryString();
			String fullRequestUri = contextPath + requestUri;
			if(null != queryStr && !"".equals(queryStr)){
				fullRequestUri += "?" + queryStr;
			}
			
			if(needValidateUrlSet!=null){
				for (Iterator iter = needValidateUrlSet.iterator(); iter.hasNext();) {
					String url = contextPath + (String) iter.next();
					if(fullRequestUri.indexOf(url) != -1){
						if(securityContext!=null && !securityContext.hasUrlPermission(url)){
							response.sendRedirect(contextPath + "/jsp/common/nopermission.jsp");
							return;
						}
					}
				}
			}
			
			filterChain.doFilter(servletRequest, servletResponse);

		} catch (ServletException sx) {
			 logger.error(sx);
			
		} catch (IOException iox) {
		  logger.error(iox);
		}

	}

	/**功能权限验证
	 * @param securityContext
	 * @return
	 */
	private boolean validatePermission(CmSecurityContext securityContext, String requestUri, String contextPath) {
	  if(securityContext==null) {
	    return true;
	  }
	  
		if(!isSkipValidate(requestUri, contextPath)){
			if(!securityContext.hasUrlPermission(requestUri)) {
				return false;
			}
		}
		
		return true;
	}

	/**验证session
	 * @param securityContext
	 * @param contextPath 
	 * @param requestUri 
	 * @return
	 */
	private boolean validateSession(CmSecurityContext securityContext, String requestUri, String contextPath) {
		if(!isSkipSessionValidate(requestUri, contextPath)) {
			if(securityContext == null) {
				return false;
			}
		}
		
		return true;
	}

	private boolean isSkipSessionValidate(String requestUri, String contextPath){
		for (Iterator iterator = skipSessionValidateDirSet.iterator(); iterator.hasNext();) {
			String page = (String) iterator.next();
			if(requestUri.indexOf(page) != -1){
				return true ;
			}
			
		}
		
		return false ;
	}	

	private boolean isSkipValidate(String requestUri, String contextPath){
		//先根据目录过滤
		for (Iterator iterator = skipValidateDirSet.iterator(); iterator.hasNext();) {
			String dir = (String) iterator.next();
			if(requestUri.indexOf(dir) != -1){
				return true ;
			}
			
		}
		
		//再过滤具体请求
		if(skipValidatePageSet.contains(requestUri)) {
			return true;
		}
		
		return false ;
	}	
	
	public void destroy() {

	}
}