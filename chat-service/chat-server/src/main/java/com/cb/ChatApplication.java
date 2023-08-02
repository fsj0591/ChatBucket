package com.cb;

import com.cb.common.data.annotation.EnableAutoFill;
import com.cb.common.web.annotation.EnableDefaultExceptionAdvice;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fsj0591
 */
@SpringBootApplication
@MapperScan("com.cb.chat.mapper")
@EnableDefaultExceptionAdvice
@EnableAutoFill
public class ChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class);
	}

}
