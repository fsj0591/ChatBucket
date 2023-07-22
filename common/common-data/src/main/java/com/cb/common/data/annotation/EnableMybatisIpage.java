package com.cb.common.data.annotation;

import com.cb.common.data.config.MybatisPlusPageConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MybatisPlusPageConfig.class)
public @interface EnableMybatisIpage {
}
