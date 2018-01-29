package com.lj.app.core.common.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 简单树
 *
 */
public class SimpleTree extends TagSupport{
	
	private final static String DATA_TYPE_JSON = "json";
	private final static String DATA_TYPE_XML = "xml";
	private final static String DATA_TYPE_HTML = "html";
	
	private final static String SWITCH_TYPE_CLIENT = "client";
	private final static String SWITCH_TYPE_AJAX = "ajax";
	
	
	private String dataType = DATA_TYPE_JSON ;// xml json html 
	private String data ;//data 
	private String iconPath = "${ctx}/scripts/tree/img/";
	private String ajaxAction ;//action
	private String switchType = SWITCH_TYPE_CLIENT ; //
	private String renderTo = ""; //
	
	private String focusNodeId ;
	private String focusNodeName ;
	
	private String checked ;
	
	
	
	public int doStartTag() throws JspException {
		
		StringBuilder treeBuf = new StringBuilder();
		
		treeBuf.append("<script>");
		treeBuf.append("tree = new TreePanel(");
		treeBuf.append("{");
		
		//parameters
		treeBuf.append("'root':" ).append(data).append(",");
		if(iconPath != null && !iconPath.equals("")){
			treeBuf.append("'iconPath':'").append(iconPath).append("',");
		}
		
		treeBuf.append("'switchType':'").append(switchType).append("',");
		if(switchType.equals(SWITCH_TYPE_AJAX)){
			treeBuf.append("'ajaxAction':'").append(ajaxAction).append("',");
		}
		
		treeBuf.append("'renderTo':'").append(renderTo).append("',");
		if(renderTo.equals("")){
			treeBuf.append("'renderTo':'").append(renderTo).append("',");
		}
		
		treeBuf.deleteCharAt(treeBuf.length()-1);
		treeBuf.append("}");
		treeBuf.append(");");
		
		//render
		treeBuf.append("tree.render();");
		treeBuf.append("</script>");

		print(treeBuf.toString());
		
		return SKIP_BODY;
	}
	
	private void print(String s){
		JspWriter write = pageContext.getOut();
		try {
			write.print(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getAjaxAction() {
		return ajaxAction;
	}

	public void setAjaxAction(String ajaxAction) {
		this.ajaxAction = ajaxAction;
	}

	public String getSwitchType() {
		return switchType;
	}

	public void setSwitchType(String switchType) {
		this.switchType = switchType;
	}

	public String getFocusNodeId() {
		return focusNodeId;
	}

	public void setFocusNodeId(String focusNodeId) {
		this.focusNodeId = focusNodeId;
	}

	public String getFocusNodeName() {
		return focusNodeName;
	}

	public void setFocusNodeName(String focusNodeName) {
		this.focusNodeName = focusNodeName;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getRenderTo() {
		return renderTo;
	}

	public void setRenderTo(String renderTo) {
		this.renderTo = renderTo;
	}
	
}
