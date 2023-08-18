package com.cb.user.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_role")
public class Role {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 角色
     */
    private Integer role;

}
