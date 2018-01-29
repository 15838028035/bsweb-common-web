package com.lj.app.core.common.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.lj.app.core.common.base.entity.BaseEntity;
import com.lj.app.core.common.base.service.BaseService;
import com.lj.app.core.common.exception.BusinessException;
import com.lj.app.core.common.pagination.Page;
import com.lj.app.core.common.pagination.PageTool;
import com.lj.app.core.common.util.AjaxResult;
import com.lj.app.core.common.util.DateUtil;
import com.lj.app.core.common.util.SessionCode;
import com.lj.app.core.common.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * Struts2中典型CRUD Action的抽象基类.
 * 
 * 主要定义了对Preparable,ModelDriven接口的使用,以及CRUD函数和返回值的命名.
 *
 * @param <T> CRUDAction所管理的对象类型.
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractBaseAction<T> extends ActionSupport implements ModelDriven<T>, Preparable {

  private static Log logger = LogFactory.getLog(AbstractBaseAction.class);
  

	/** 进行增删改操作后,以redirect方式重新打开action默认页的result名.*/
	public static final String RELOAD = "reload";
	
	public static final String REDIRECT = "redirect";
	
	public static final String INPUT = "input";
	public static final String EDIT = "edit";
	public static final String SAVE = "save";
	public static final String LIST = "list";
	public static final String VIEW = "view";
	
	
	protected Page<T> page = new Page<T>(PAGESIZE);
	
	public static int PAGESIZE = 20;
	
	public static final String CREATE_SUCCESS = "保存成功";
	public static final String UPDATE_SUCCESS = "修改成功";
	public static final String DELETE_SUCCESS = "删除成功";
	
	public static final String CREATE_FAILURE = "保存失败";
	public static final String UPDATE_FAILURE = "修改失败";
	public static final String DELETE_FAILURE = "删除失败";
	
	public static final String OPT_SUCCESS = "操作成功";
	public static final String OPT_FAILURE = "操作失败";
	
	public static final String BATCH_IMPORT = "batchimport";
	public static final String BATCH_IMPORT_SUCCESS = "批量导入成功";
	public static final String BATCH_IMPORT_FALIRUE = "批量导入失败";
	
	public static final String OK = "ok";
	public static final String ERROR = "error";
	
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	public static final String CREATE_BY = "createBy";
	
	protected  String returnMessage = "";
	
	protected String multidelete;
	protected String multiSelected;
	protected String multidExecute;
	
	public  String operate;//操作
	
	private String sidx;
	private String sord;
	
	protected String appId;
	
	private String sortName;//排序名称
	private String sortOrder;//排序方式
	
	// 与jsp表单中的名称对应
	protected File templateFile;
	// FileName为固定格式
	protected String templateFileFileName;
	// ContentType为固定格式
	protected String templateFileContentType;
	
	protected String  conditionWhere;//页面查询
		
	public String getMultidExecute() {
		return multidExecute;
	}

	public void setMultidExecute(String multidExecute) {
		this.multidExecute = multidExecute;
	}
	
	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	
	public Page<T> getPage() {
		return page;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}

	public String getMultidelete() {
		return multidelete;
	}

	public void setMultidelete(String multidelete) {
		this.multidelete = multidelete;
	}
	
	public String getMultiSelected() {
		return multiSelected;
	}

	public void setMultiSelected(String multiSelected) {
		this.multiSelected = multiSelected;
	}
	
	public File getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(File templateFile) {
		this.templateFile = templateFile;
	}

	public String getTemplateFileFileName() {
		return templateFileFileName;
	}

	public void setTemplateFileFileName(String templateFileFileName) {
		this.templateFileFileName = templateFileFileName;
	}

	public String getTemplateFileContentType() {
		return templateFileContentType;
	}

	public void setTemplateFileContentType(String templateFileContentType) {
		this.templateFileContentType = templateFileContentType;
	}

	public String getConditionWhere() {
		return conditionWhere;
	}

	public void setConditionWhere(String conditionWhere) {
		this.conditionWhere = conditionWhere;
	}

	public int getLoginUserId() {
		return (Integer)Struts2Utils.getSessionAttribute(SessionCode.MAIN_ACCT_ID);
	}
	
	public String getLoginUserName() {
		return  (String)Struts2Utils.getSessionAttribute(SessionCode.LOGIN_NAME);
	}
	
	public String getUserName() {
		return  (String)Struts2Utils.getSessionAttribute(SessionCode.USER_NAME);
	}
	
	/**
	 * Action函数, 默认的action函数, 默认调用list()函数.
	 */
	@Override
	public String execute() throws Exception {
		return list();
	}

	//-- CRUD Action函数 --//
	/**
	 * Action函数,显示Entity列表界面.
	 * 建议return SUCCESS.
	 */
	public abstract String list() throws Exception;

	/**
	 * Action函数,显示新增或修改Entity界面.
	 * 建议return INPUT.
	 */
	@Override
	public abstract String input() throws Exception;

	/**
	 * Action函数,新增或修改Entity. 
	 * 建议return RELOAD.
	 */
	public abstract String save() throws Exception;

	/**
	 * Action函数,删除Entity.
	 * 建议return RELOAD.
	 */
	public abstract String delete() throws Exception;

	//-- Preparable函数 --//
	/**
	 * 实现空的prepare()函数,屏蔽所有Action函数公共的二次绑定.
	 */
	public void prepare() throws Exception {
	}

	/**
	 * 在input()前执行二次绑定.
	 */
	public void prepareInput() throws Exception {
		prepareModel();
	}

	/**
	 * 在save()前执行二次绑定.
	 */
	public void prepareSave() throws Exception {
		prepareModel();
	}

	/**
	 * 等同于prepare()的内部函数,供prepardMethodName()函数调用. 
	 */
	protected abstract void prepareModel() throws Exception;
	
	/**
	 * 公共jgGrid查询方法
	 * @return
	 * @throws Exception
	 */
	public String jqGridList() throws Exception {
		try {
			Map<String,Object> condition = new HashMap<String,Object>();
			page.setFilters(getModel());
			
			if (StringUtil.isNotBlank(this.getSidx())) {
				String orderBy = PageTool.convert(this.getSidx()) + " "+ this.getSord();
				page.setSortColumns(orderBy);
			}
			
			page = getBaseService().findPageList(page, condition);
			Struts2Utils.renderText(PageTool.pageToJsonJQGrid(this.page),new String[0]);
			return null;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}
	
	/**
	 * 公共bootStrapList查询方法
	 * @return
	 * @throws Exception
	 */
	public String bootStrapList() throws Exception {
		Map<String,Object> condition = new HashMap<String,Object>();
		return bootStrapListCommon(condition);
	}
	
	/**
	 * 公共bootStrapList查询方法
	 * @return
	 * @throws Exception
	 */
	public String bootStrapListCommon(Map<String,Object> condition ) throws Exception {
		try {
			page.setFilters(getModel());
			
			if (StringUtil.isNotBlank(this.getSortName())) {
				String orderBy = PageTool.convert(this.getSortName()) + " "+ this.getSortOrder();
				page.setSortColumns(orderBy);
			}
			
			if (StringUtil.isNotBlank(conditionWhere)) {
				condition.put("conditionWhere", conditionWhere);
			}
			
			page = getBaseService().findPageList(page, condition);
			Struts2Utils.renderText(PageTool.pageToJsonBootStrap(this.page),new String[0]);
			return null;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}
	
	/**
	 * 公共保存或者更新方法
	 * @return
	 * @throws Exception
	 */
	public String commonSaveOrUpdate() throws Exception {
		
	try{
			if (StringUtil.isEqualsIgnoreCase(operate, AbstractBaseAction.EDIT)) {
				BaseEntity entity = (BaseEntity)getModel();
				entity.setUpdateBy(this.getLoginUserId());
				entity.setUpdateByUname(this.getUserName());
				entity.setUpdateDate(DateUtil.getNowDateYYYYMMddHHMMSS());
				
				getBaseService().updateObject(entity);
				returnMessage = UPDATE_SUCCESS;
			}else{
				BaseEntity entity = (BaseEntity)getModel();
				entity.setCreateBy(this.getLoginUserId());
				entity.setCreateByUname(this.getUserName());
				entity.setCreateDate(DateUtil.getNowDateYYYYMMddHHMMSS());
				
				getBaseService().insertObject(entity);
				returnMessage = CREATE_SUCCESS;
			}
			
			return LIST;
		}catch(Exception e){
			returnMessage = CREATE_FAILURE;
			logger.error(e);
			throw e;
		}
	}
	
	public String multidelete() throws Exception {
		String returnMessage = "";
		String[] multideleteTemp;
		if (multidelete.indexOf(",") > 0) {
			multideleteTemp = multidelete.split(",");
		}
		else{
			multideleteTemp = new String[]{multidelete};
		}
		for (int i = 0; i < multideleteTemp.length; i++) {
			if(StringUtil.isBlank(multideleteTemp[i])){
				continue;
			}
			int deleteId = Integer.parseInt(multideleteTemp[i].trim());
			
			try{
				multideleteValidate(deleteId);
				// 循环删除
				getBaseService().delete(deleteId);
			}catch(BusinessException e){
				returnMessage = e.getMessage();
				logger.error(e);
			}
			catch(Exception e){
				returnMessage = "删除失败";
				logger.error(e);
			}
		}
		AjaxResult ar = new AjaxResult();
		if (returnMessage.equals("")) {
			ar.setOpResult("删除成功");//删除成功！
		}else{
			ar.setOpResult(returnMessage);
		}
		
		Struts2Utils.renderJson(ar);
		return null;
	}
	
	/**
	 * 删除校验
	 * @param deleteId
	 * @throws Exception
	 */
	public void multideleteValidate(Integer deleteId) throws BusinessException {
		
	}
	
	public String downloadExcelTemplates() throws IOException {
		return downloadTemplates("xls");
	}
	
	public String downloadXmlTemplates() throws IOException {
		return downloadTemplates("xml");
	}
	
	public String downloadTemplates(String templateType) throws IOException {
		String filetype = ServletActionContext.getRequest().getParameter("filetype");
		String templateDir = ServletActionContext.getRequest().getParameter("templateDir");
		String template = ServletActionContext.getRequest().getParameter("template");
				
		String contextpath = ServletActionContext.getServletContext().getRealPath("/");
		
		if(StringUtils.isBlank(templateDir)){
			templateDir = "templateDir";
		}
				
		String filepath = contextpath + File.separator+templateDir + File.separator+ filetype + "."+templateType;
		download(filepath, template,
				ServletActionContext.getResponse().getOutputStream());
		return null;
	}

	public void download(String filepath, String filedisplay, OutputStream out)
			throws IOException {
		File f = new File(filepath);
		if(f==null || !f.exists()){
			f.createNewFile();
		}
		
		FileInputStream in = new FileInputStream(f);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[2048];
		int i = 0;
		while ((i = in.read(b)) != -1) {
			baos.write(b, 0, i);
		}

		ServletActionContext.getResponse().setContentType("bin");
		filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
		ServletActionContext.getResponse().addHeader("Content-Disposition",
				"attachment;filename=" + filedisplay);
		baos.writeTo(out);
		out.flush();
		in.close();
	}
	
	public abstract  BaseService<T> getBaseService();
}
