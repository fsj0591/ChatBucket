package com.cb.user.pojo.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField("name")
    private String name;

    /**
     * 密码,md5加密
     */
    @TableField("password")
    private String password;

    /**
     * 邮箱
     */
    @TableField("mail")
    private String mail;

    /**
     * 头像
     */
    @TableField("image")
    private String image;

    /**
     * 0 男
     1 女
     2 未知
     */
    @TableField("sex")
    private Boolean sex;

    /**
     * 是否身份认证
     */
    @TableField("identity")
    private Boolean identity;

    /**
     * 贵宾到期时间
     */
    @TableField("vip")
    private Timestamp vip;

    /**
     * 注册时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createdTime;

}
