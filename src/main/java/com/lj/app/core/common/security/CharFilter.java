package com.lj.app.core.common.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.util.HtmlUtils;


@SuppressWarnings("unchecked")
public class CharFilter implements Filter {

	private FilterConfig filterConfig = null;

	private static Map<String, String> wordMap = null;
	
	/**
	 * 配置不需要进行特殊字符校验的url
	 */
	private static List<String> excludeUrl = null;
	
	static {
		excludeUrl = new ArrayList<String>();
		excludeUrl.add("/jsp/common/acctauthor!showAcctAuthorDetail.action"); 
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
	    servletRequest.setCharacterEncoding("UTF-8");
	    servletResponse.setCharacterEncoding("UTF-8");
	    servletResponse.setContentType("text/html;charset=" + "UTF-8");
	    
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		Enumeration<String> paramNames = request.getParameterNames();
		boolean isExcluded = listHasUrl(request.getRequestURI());
		while (paramNames.hasMoreElements() && !isExcluded) {
			String name = paramNames.nextElement();//取出参数名称
			String values[] = request.getParameterValues(name);//根据参数名称取出其值
			
			if(values==null){
				continue;
			}
			for (int i = 0; i < values.length; i++) {
				if(values[i]!=null){
					if(!name.equals("uacBulletin.content") && !name.equals("uacBulletin.title") && !name.equals("acctAuthorInfo")){
						values[i] = HtmlUtils.htmlEscape(values[i]);
					}
					values[i] = StringEscapeUtils.escapeSql(values[i]);
					String _value = StringEscapeUtils.escapeJavaScript(values[i]);
					if(Pattern.matches("^.*(%3C|<|&lt;|%253c|%3c).*((?i)script).*(%3E|>|&gt;|%253e|%3e).*$",_value)
							|| Pattern.matches("^.*alert(%28|\\(|%2528).*(%29|\\)|%2529).*$", _value)
							|| Pattern.matches("^.*(%3C|<|&lt;|%253c|%3c).*$", _value) 
							|| Pattern.matches("^.*(%3E|>|&gt;|%253e|%3e).*$", _value)
							|| Pattern.matches("^.*(%0A|%250A|%0a|%250a).*$", _value)){
						//不做 处理  response.sendRedirect(request.getContextPath()+"/jsp/common/nopermission.jsp");
						//return;
					} 
				}	
			}
			
			/*replaceChar(values);
			
			for (int i = 0; i < values.length; i++) {
				request.setAttribute(name, values[i]);
			}*/
			
		}

		chain.doFilter(servletRequest, servletResponse);
	}

	/**替换字符
	 * @param values
	 */
	private void replaceChar(String[] values) {
		Iterator iter = wordMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry me = (Map.Entry) iter.next();
			String key = (String) me.getKey();
			String value = (String) me.getValue();
			for (int i = 0; i < values.length; i++) {
				values[i] = values[i].replaceAll(key, value).trim();
			}
		}
	}
	
	private boolean listHasUrl(String url){
		for (int i = 0; i < excludeUrl.size(); i++) {
			String _exclude = excludeUrl.get(i);
			if(url.indexOf(_exclude) != -1){
				return true;
			}
		}
		return false;
	}

	public void destroy() {
	}

	public void init(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

	public void setFilterConfig(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	public static Map<String, String> getWordMap() {
		return wordMap;
	}

	public static void setWordMap(Map<String, String> wordMap) {
		CharFilter.wordMap = wordMap;
	}

	public static List<String> getExcludeUrl() {
		return excludeUrl;
	}

	public static void setExcludeUrl(List<String> excludeUrl) {
		CharFilter.excludeUrl = excludeUrl;
	}
	
}
