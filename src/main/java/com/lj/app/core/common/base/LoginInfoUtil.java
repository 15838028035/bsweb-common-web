package com.lj.app.core.common.base;

import com.lj.app.core.common.security.CmSecurityContext;
import com.lj.app.core.common.security.SecurityConstants;
import com.lj.app.core.common.web.Struts2Utils;

/**
 * 
 * 登陆信息工具类
 *
 */
public class LoginInfoUtil {

  private LoginInfoUtil() {
  
  }
  
  /**
   * 得到当前登录主帐号ID
   * 
   * @return ID
   */
  public static long getLoginMainAcctId() {
    CmSecurityContext securityContext = (CmSecurityContext) Struts2Utils
        .getSessionAttribute(SecurityConstants.SECURITY_CONTEXT);
    long operatorId = securityContext.getMainAcctId();
    return operatorId;
  }
  
  /**
   * 得到当前登录主帐号Name
   * 
   * @return 登陆名称
   */
  public static String getLoginMainAcctName() {
    CmSecurityContext securityContext = (CmSecurityContext) Struts2Utils
        .getSessionAttribute(SecurityConstants.SECURITY_CONTEXT);
    String loginName = securityContext.getLoginName();
    return loginName;
  }

}
