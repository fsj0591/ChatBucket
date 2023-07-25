package com.cb.user.pojo.dto;

import lombok.Data;

@Data
public class LoginDto {

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 密码
     */
    private String password;
}
