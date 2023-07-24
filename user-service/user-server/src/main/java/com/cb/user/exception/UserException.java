package com.cb.user.exception;

/**
 * 用户服务异常
 */
public class UserException extends RuntimeException {

	public UserException() {
		super("用户服务异常");
	}

	public UserException(String message) {
		super(message);
	}

}
