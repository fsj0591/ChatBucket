package com.cb.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cb.user.pojo.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Update("update user set vip = #{time} where id = #{userId}")
    Integer createVIP(@Param("userId") Integer userId, @Param("time") Timestamp time);
}
