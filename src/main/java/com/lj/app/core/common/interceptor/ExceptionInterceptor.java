package com.lj.app.core.common.interceptor;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;

import com.lj.app.core.common.exception.BusinessException;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ExceptionInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -8096551528633660568L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String result = "globalError";
		try {
			result = invocation.invoke();
			System.out.println("*******************\n" + result);
		} catch (DataAccessException e) {
			throw new BusinessException("数据库操作失败！");
		} catch (NullPointerException e) {
			throw new BusinessException("调用了未经初始化的对象或者是不存在的对象。");
		} catch (IOException e) {
			throw new BusinessException("IO异常。");
		} catch (ClassNotFoundException e) {
			throw new BusinessException("指定的类不存在。");
		} catch (ArithmeticException e) {
			throw new BusinessException("数学运算异常。");
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new BusinessException("数组下标越界。");
		} catch (IllegalArgumentException e) {
			throw new BusinessException("方法的参数错误。");
		} catch (ClassCastException e) {
			throw new BusinessException("类型强制转换错误。");
		} catch (SecurityException e) {
			throw new BusinessException("违背安全原则异常。");
		} catch (SQLException e) {
			throw new BusinessException("操作数据库异常。");
		} catch (NoSuchMethodError e) {
			throw new BusinessException("方法末找到异常。");
		} catch (InternalError e) {
			throw new BusinessException("Java虚拟机发生了内部错误");
		} catch (Exception e) {
			throw new BusinessException("程序内部错误，操作失败。");
		}
		// after(invocation, result);
		System.out.println("\nexception interceptor----------------------------\n"+ result);
		return result;

	}

}