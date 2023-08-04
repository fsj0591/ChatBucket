package com.cb.chat.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName("favorites")
public class Favorites implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    Integer id;

    /**
     * 收藏夹名称
     */
    String favoritename;

    /**
     * 用户id
     */
    Integer userId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    Timestamp createdTime;

}
