package com.lj.app.core.common.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AppSecurityTag extends SimpleTagSupport {

  private static Log logger = LogFactory.getLog(AppSecurityTag.class);
  

	private String code;
	private String required;
	private String appCode;
	private Long adminAcctSeq;

	public void doTag() throws JspException, IOException {

		boolean isShow = true;
		if (required == null || required.equals("true")) {
			if (code != null) {
				String[] ops = code.split(",");
				if (ops != null && ops.length == 2) {
					try {
						//TODO:实现权限校验
					} catch (Exception e) {
						logger.error(e);
					}
				}
			}
		}

		if (isShow) {
			this.getJspBody().invoke(this.getJspContext().getOut());
		}
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public void setAdminAcctSeq(Long adminAcctSeq) {
		this.adminAcctSeq = adminAcctSeq;
	}
	public String getCode() {
		return code;
	}
	public String getRequired() {
		return required;
	}
	public String getAppCode() {
		return appCode;
	}
	public Long getAdminAcctSeq() {
		return adminAcctSeq;
	}
	
	
}