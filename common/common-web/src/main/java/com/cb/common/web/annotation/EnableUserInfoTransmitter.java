package com.cb.common.web.annotation;

import com.cb.common.web.config.UserInfoTransmitterHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Fegin调用时用户信息的传递
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(UserInfoTransmitterHandler.class)
public @interface EnableUserInfoTransmitter {
}
