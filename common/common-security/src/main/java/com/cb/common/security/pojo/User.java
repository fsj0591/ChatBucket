package com.cb.common.security.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class User {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 密码,md5加密
     */
    private String password;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 头像
     */
    private String image;

    /**
     * 0 男
     1 女
     2 未知
     */
    private Boolean sex;

    /**
     * 是否身份认证
     */
    private Boolean identity;

    /**
     * 贵宾到期时间
     */
    private Timestamp vip;

    /**
     * 注册时间
     */
    private Timestamp createdTime;

}
