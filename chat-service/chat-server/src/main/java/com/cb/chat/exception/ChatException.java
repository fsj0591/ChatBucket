package com.cb.chat.exception;


import com.cb.common.core.exception.BusinessException;

/**
 * 聊天异常类
 */
public class ChatException extends BusinessException {

	public ChatException() {
		super("聊天服务异常");
	}

	public ChatException(String errMessage) {
		super(errMessage);
	}

}
