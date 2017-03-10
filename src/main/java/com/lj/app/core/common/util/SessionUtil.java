package com.lj.app.core.common.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.lj.app.core.common.base.entity.UpmUser;
import com.lj.app.core.common.security.CMSecurityContext;
import com.lj.app.core.common.security.SecurityConstants;
import com.lj.app.core.common.web.Struts2Utils;

public class SessionUtil {
	
	private static HttpSession session;
	
	public static UpmUser getUser() {
		UpmUser upmUser = (UpmUser) getHttpSession().getAttribute(SessionCode.MAIN_ACCT);
		return upmUser;
	}

	public static void setuser(UpmUser upmUser) {
		getHttpSession().setAttribute(SessionCode.MAIN_ACCT, upmUser);
	}

	public static long getMainAcctId() {
		CMSecurityContext securityContext = (CMSecurityContext) Struts2Utils.getSessionAttribute(SecurityConstants.SECURITY_CONTEXT);
		if (securityContext == null) {
			UpmUser upmUser  = getUser();
			return upmUser == null ? null : upmUser.getId();
		}
		return securityContext.getMainAcctId();
	}

	public static String getUserName() {
		UpmUser upmUser = getUser();
		return upmUser == null ? null : upmUser.getUserName();
	}

	public static Object getSessionAttribute(String key) {
		return getHttpSession().getAttribute(key);
	}

	public static Map<String,Object> getSessionMap() {
		CMSecurityContext securityContext = (CMSecurityContext) Struts2Utils.getSessionAttribute(SecurityConstants.SECURITY_CONTEXT);
		String loginName = securityContext.getLoginName();
		Map<String,Object> sessionMap = new HashMap<String, Object>();
		sessionMap.put(SessionCode.LOGIN_NAME, loginName);
		return sessionMap;
	}
	
	public static HttpSession getHttpSession() {
		if(session == null){
			session =Struts2Utils.getSession();
		}
		return session;
	}
	
	public static void setHttpSession(HttpSession httpSession){
		session = httpSession;
	}
}
