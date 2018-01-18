package com.lj.app.core.common.base;

import com.lj.app.core.common.security.CmSecurityContext;
import com.lj.app.core.common.security.SecurityConstants;
import com.lj.app.core.common.web.Struts2Utils;

public class LoginInfoUtil {
	
  	/**得到当前登录主帐号ID
  	 * @return
  	 */
  	public static long getLoginMainAcctId() {
  		CmSecurityContext securityContext = (CmSecurityContext) Struts2Utils.getSessionAttribute(SecurityConstants.SECURITY_CONTEXT);
  		long operatorId = securityContext.getMainAcctId();
  		return operatorId;
  	}
  	
  	/**得到当前登录主帐号Name
  	 * @return
  	 */
  	public static String getLoginMainAcctName() {
  		CmSecurityContext securityContext = (CmSecurityContext) Struts2Utils.getSessionAttribute(SecurityConstants.SECURITY_CONTEXT);
  		String loginName = securityContext.getLoginName();
  		return loginName;
  	}
	
}
