package com.lyl.layuiadmin.exceptions;

import lombok.Data;

/**
 * 检查异常
 * @author Developer
 *
 */
@Data
public class CheckException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String errorCode;

	/**
	 * 构造函数
	 * */
	public CheckException(String code,String message) {
		super(message);
		this.errorCode = code;
	}

	public CheckException() {
	}

	public CheckException(String message) {
		super(message);
	}

	public CheckException(Throwable cause) {
		super(cause);
	}

	public CheckException(String message, Throwable cause) {
		super(message, cause);
	}

}
