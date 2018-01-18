package com.lj.app.core.common.security;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class SecurityTag3 extends SimpleTagSupport {
	
	private String code;
	
	private String required;

	public void setCode(String code) {
		this.code = code;
	}
	
	public void setRequired(String required) {
		this.required = required;
	}

	public String getCode() {
		return code;
	}

	public String getRequired() {
		return required;
	}

	public void doTag() throws JspException, IOException {
		if(required == null || required.equals("true")) {
			CmSecurityContext securityContext = (CmSecurityContext)this.getJspContext().findAttribute(
					SecurityConstants.SECURITY_CONTEXT);
			
			if (code == null || securityContext == null)
				throw new JspException("securityContext or code is null");
			
			if(securityContext.hasDisplayPermission(code)) {
				getJspBody().invoke(this.getJspContext().getOut());
			}
		} else {
			getJspBody().invoke(this.getJspContext().getOut());
		}
		
	}
}