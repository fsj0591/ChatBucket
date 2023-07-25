package com.cb.user.exception;

import com.cb.common.core.exception.BusinessException;

/**
 * 用户服务异常
 */
public class UserException extends BusinessException {

	public UserException() {
		super("用户服务异常");
	}

	public UserException(String message) {
		super(message);
	}

}
