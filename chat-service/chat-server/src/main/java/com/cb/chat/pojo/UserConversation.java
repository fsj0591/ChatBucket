package com.cb.chat.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_conversation")
public class UserConversation {

    Integer userId;

    @TableId
    Integer conversation;

}
