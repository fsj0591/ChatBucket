package com.cb.user;

import com.cb.common.data.annotation.EnableAutoFill;
import com.cb.common.data.annotation.EnableRedisSerialize;
import com.cb.common.web.annotation.EnableDefaultExceptionAdvice;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fsj0591
 */
@SpringBootApplication
@MapperScan("com.cb.user.mapper")
@EnableDefaultExceptionAdvice
@EnableRedisSerialize
@EnableAutoFill
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class);
	}

}
