package com.cb.common.data.annotation;

import com.cb.common.data.config.AutoFillConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AutoFillConfig.class)
public @interface EnableAutoFill {
}
