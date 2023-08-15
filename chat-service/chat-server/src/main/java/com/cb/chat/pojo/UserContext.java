package com.cb.chat.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_context")
public class UserContext {

    @TableId
    private Long id;

    /**
     * 用户名
     */
    private Integer conversation;

    /**
     * 角色
     */
    private String role;

    /**
     * 信息
     */
    private String content;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createdTime;

}
