package com.cb.common.web.annotation;

import com.cb.common.web.config.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Web程序异常捕获响应异常信息
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(GlobalExceptionHandler.class)
public @interface EnableDefaultExceptionAdvice {

}
