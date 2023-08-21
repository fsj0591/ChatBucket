package com.cb.user;

import com.cb.common.data.annotation.EnableAutoFill;
import com.cb.common.data.annotation.EnableRedisSerialize;
import com.cb.common.web.annotation.EnableDefaultExceptionAdvice;
import com.cb.common.web.annotation.EnableUserInfoTransmitter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author fsj0591
 */
@SpringBootApplication(scanBasePackages = {"com.cb.user","com.cb.common.security"})
@MapperScan("com.cb.user.mapper")
@EnableDefaultExceptionAdvice
@EnableRedisSerialize
@EnableAutoFill
@EnableUserInfoTransmitter
@EnableFeignClients(basePackages = {"com.cb.chat.client"})
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class);
	}

}
