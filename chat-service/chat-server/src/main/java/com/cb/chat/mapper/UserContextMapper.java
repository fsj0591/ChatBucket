package com.cb.chat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cb.chat.model.completion.chat.ChatMessage;
import com.cb.chat.pojo.UserContext;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserContextMapper extends BaseMapper<UserContext> {
    @Select("select role,content from user_context where conversation=#{conversation}")
    List<ChatMessage> selectMessageForUser(@Param("conversation") Integer conversation);
}
